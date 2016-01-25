package net.dean.jraw.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.HttpRequest;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.RestResponse;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.InvalidScopeException;

/**
 * This class enhances the RedditClient by taking advantage of the Android platform. Using the
 * {@link #AndroidRedditClient(Context)} constructor, the UserAgent may be automagically determined from the app's
 * manifest.
 */
public class AndroidRedditClient extends RedditClient {
    private static final String KEY_USER_AGENT_OVERRIDE = "net.dean.jraw.USER_AGENT_OVERRIDE";
    private static final String KEY_REDDIT_USERNAME =     "net.dean.jraw.REDDIT_USERNAME";
    private static final String PLATFORM =                "android";

    private static UserAgent getUserAgent(Context context) {
        try {
            // Get the app's <meta-data> tags from the manifest
            Bundle bundle = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;

            // If there are no <meta-data> tags the Bundle will be null
            if (bundle == null)
                throw new IllegalStateException("Please specify a <meta-data> for either " + KEY_REDDIT_USERNAME +
                        " or " + KEY_USER_AGENT_OVERRIDE);

            String userAgent = bundle.getString(KEY_USER_AGENT_OVERRIDE, null);
            if (userAgent != null)
                return UserAgent.of(userAgent);

            String username = bundle.getString(KEY_REDDIT_USERNAME, null);
            if (username == null)
                throw new IllegalStateException("No <meta-data> for " + KEY_REDDIT_USERNAME);
            return UserAgent.of(PLATFORM, context.getPackageName(), BuildConfig.VERSION_NAME, username);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException("Could not find package metadata for own package", e);
        }
    }

    /**
     * Attempts construct a RedditClient by resolving a {@link UserAgent} from a given Context. In order to be
     * successful, then one of two parameters must be specified in the AndroidManifest.xml file.
     *
     * <p>For ease of use, include a &lt;meta-data&gt; tag with the key {@value #KEY_REDDIT_USERNAME}, the value being
     * the developer's reddit username. The package name and build version will be determined programmatically through
     * the given Context.
     *
     * <p>If the developer wishes to specify a UserAgent against the recommended formula, they must include a
     * &lt;meta-data&gt; tag with the key {@value #KEY_USER_AGENT_OVERRIDE}, the value being the String to be passed to
     * {@link UserAgent#of(String)}.
     *
     * <p>If neither of these options are appealing, one may use the traditional constructor:
     * {@link #AndroidRedditClient(UserAgent)}.
     *
     * @throws IllegalStateException If neither of the metadata elements are present
     */
    public AndroidRedditClient(Context context) {
        this(getUserAgent(context));
    }

    /** Traditional RedditClient constructor */
    public AndroidRedditClient(UserAgent userAgent) {
        super(userAgent);
    }

    @Override
    public RestResponse execute(HttpRequest request) throws NetworkException, InvalidScopeException {
        if (getUserAgent().trim().isEmpty()) {
            throw new IllegalStateException("No UserAgent specified");
        }
        return super.execute(request);
    }
}
