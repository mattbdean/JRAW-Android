# JRAW-Android

This is an extension to the [Java Reddit API Wrapper](https://github.com/mattbdean/JRAW) that adds some Android-specific classes.

## Getting Started

Before using this library it is highly recommended that you first read the [OAuth2 page](https://mattbdean.gitbooks.io/jraw/oauth2.html) in the JRAW documentation.

First create a reddit OAuth2 app [here](https://www.reddit.com/prefs/apps). Note the client ID and redirect URL, you'll need these later.

Add these `<meta-data>` keys to your manifest:

```xml
<application>
    ...
    <meta-data
        android:name="net.dean.jraw.android.REDDIT_USERNAME"
        android:value="(...)" />
    <meta-data
        android:name="net.dean.jraw.android.CLIENT_ID"
        android:value="(...)" />
    <meta-data
        android:name="net.dean.jraw.android.REDIRECT_URL"
        android:value="(...)" />
</application>
```

The `REDDIT_USERNAME` key is used to create a UserAgent for your app. See [here](https://github.com/mattbdean/JRAW-Android/blob/1.0.0/lib/src/main/kotlin/net/dean/jraw/android/ManifestAppInfoProvider.kt) for more details.

Create your `Application` class:

```java
public final class App extends Application {
    private static AccountHelper accountHelper;
    private static SharedPreferencesTokenStore tokenStore;

    @Override
    public void onCreate() {
        super.onCreate();

        // Get UserAgent and OAuth2 data from AndroidManifest.xml
        AppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());

        // Ideally, this should be unique to every device
        UUID deviceUuid = UUID.randomUUID();

        // Store our access tokens and refresh tokens in shared preferences
        tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
        // Load stored tokens into memory
        tokenStore.load();
        // Automatically save new tokens as they arrive
        tokenStore.setAutoPersist(true);

        // An AccountHelper manages switching between accounts and into/out of userless mode.
        accountHelper = AndroidHelper.accountHelper(provider, deviceUuid, tokenStore);

        // Every time we use the AccountHelper to switch between accounts (from one account to
        // another, or into/out of userless mode), call this function
        accountHelper.onSwitch(new Function1<RedditClient, Unit>() {
            @Override
            public Unit invoke(RedditClient redditClient) {
                // By default, JRAW logs HTTP activity to System.out. We're going to use Log.i()
                // instead.
                LogAdapter logAdapter = new SimpleAndroidLogAdapter(Log.INFO);

                // We're going to use the LogAdapter to write down the summaries produced by
                // SimpleHttpLogger
                redditClient.setLogger(
                        new SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter));

                // If you want to disable logging, use a NoopHttpLogger instead:
                // redditClient.setLogger(new NoopHttpLogger());

                return null;
            }
        });
    }

    public static AccountHelper getAccountHelper() { return accountHelper; }
    public static SharedPreferencesTokenStore getTokenStore() { return tokenStore; }
}
```

Now you can start using JRAW! The [example app](https://github.com/mattbdean/JRAW-Android/tree/1.0.0/example-app) fully implements the reddit authentication process. I highly encourage you to build and install the app and read the source code to get a better understanding of the whole process.

## Versioning

Unless otherwise noted, JRAW-Android's version is the same as JRAW. So JRAW-Android v1.1.0 would use JRAW v1.1.0.

## Contributing

This project uses Robolectric for unit tests. Linux and Mac users on Android Studio should see [this](http://robolectric.org/getting-started/#note-for-linux-and-mac-users) when running tests through the IDE.
