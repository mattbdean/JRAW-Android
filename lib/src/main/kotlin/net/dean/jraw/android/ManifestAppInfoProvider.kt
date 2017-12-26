package net.dean.jraw.android

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import net.dean.jraw.http.UserAgent

/**
 * This class produces [AppInfo] instances from application manifest metadata.
 *
 * Include this in your `AndroidManifest.xml`:
 *
 * ```xml
 * &lt;application android:name="foo" (...)&gt;
 *     (...)
 *
 *     &lt;meta-data
 *         android:name="net.dean.jraw.android.REDDIT_USERNAME"
 *         android:value="(your reddit username)" /&gt;
 *
 *     &lt;meta-data
 *         android:name="net.dean.jraw.android.CLIENT_ID"
 *         android:value="(your client ID)" /&gt;
 *
 *     &lt;meta-data
 *         android:name="net.dean.jraw.android.REDIRECT_URL"
 *         android:value="(your redirect URL)" /&gt;
 * &lt;/application&gt;
 * ```
 *
 * If the application's ID is "com.example.app", then the resulting AppInfo's UserAgent will be
 * something like this: `android:com.example.app:${version} (by /u/${username})` where `${version}`
 * is `BuildConfig.VERSION_NAME`.
 *
 * If this doesn't suit your needs, use `net.dean.jraw.android.USER_AGENT_OVERRIDE` instead of
 * `net.dean.jraw.android.REDDIT_USERNAME` to specify a custom user agent.
 */
class ManifestAppInfoProvider(private val context: Context) : AppInfoProvider {
    override fun provide(): AppInfo {
        // Get the app's <meta-data> tags from the manifest
        val metadata = context
                .packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                .metaData

        val ua = userAgent(metadata)

        return AppInfo(
                clientId = requireString(metadata, KEY_CLIENT_ID, "client ID"),
                redirectUrl = requireString(metadata, KEY_REDIRECT_URL, "redirect URL"),
                userAgent = ua
        )
    }

    private fun userAgent(b: Bundle): UserAgent {
        val username = b.getString(KEY_REDDIT_USERNAME)

        if (username != null)
            return UserAgent(PLATFORM, context.packageName, BuildConfig.VERSION_NAME, username)

        val override = b.getString(KEY_USER_AGENT_OVERRIDE)
        if (override != null)
            return UserAgent(override)

        throw IllegalStateException("Could produce a UserAgent from the manifest. Make sure to " +
                "include a <meta-data> tag with either the $KEY_REDDIT_USERNAME or " +
                "$KEY_USER_AGENT_OVERRIDE key.")
    }

    private fun requireString(bundle: Bundle, key: String, what: String): String {
        return bundle.getString(key) ?:
                throw IllegalStateException("Could not produce a $what from the manifest. Make " +
                        "sure to include a <meta-data> tag with the $key key.")
    }

    companion object {
        internal const val KEY_USER_AGENT_OVERRIDE = "net.dean.jraw.android.USER_AGENT_OVERRIDE"
        internal const val KEY_REDDIT_USERNAME =     "net.dean.jraw.android.REDDIT_USERNAME"
        internal const val KEY_CLIENT_ID =           "net.dean.jraw.android.CLIENT_ID"
        internal const val KEY_REDIRECT_URL =        "net.dean.jraw.android.REDIRECT_URL"

        private const val PLATFORM = "android"
    }
}
