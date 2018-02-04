# JRAW-Android

[![travis-ci build status](https://img.shields.io/travis/mattbdean/JRAW-Android.svg)](https://travis-ci.org/mattbdean/JRAW-Android)
[![Latest release](https://img.shields.io/github/release/mattbdean/JRAW.svg)](https://bintray.com/thatjavanerd/maven/JRAW-Android/_latestVersion)
[![Kotlin 1.2.10](https://img.shields.io/badge/Kotlin-1.2.10-blue.svg)](http://kotlinlang.org)
[![Codecov](https://img.shields.io/codecov/c/github/mattbdean/JRAW-Android.svg)](https://codecov.io/gh/mattbdean/JRAW-Android)

This is an extension to the [Java Reddit API Wrapper](https://github.com/mattbdean/JRAW) that adds some Android-specific classes.

## Getting Started

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'net.dean.jraw:JRAW-Android:1.0.0'
}
```

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

The `REDDIT_USERNAME` key is used to create a UserAgent for your app. See [here](https://github.com/mattbdean/JRAW-Android/blob/master/lib/src/main/kotlin/net/dean/jraw/android/ManifestAppInfoProvider.kt) for more details.

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
        accountHelper.onSwitch(redditClient -> {
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
        });
    }

    public static AccountHelper getAccountHelper() { return accountHelper; }
    public static SharedPreferencesTokenStore getTokenStore() { return tokenStore; }
}
```

Now you can start using JRAW! The [example app](https://github.com/mattbdean/JRAW-Android/tree/master/example-app) fully implements the reddit authentication process. I highly encourage you to build and install the app and read the source code to get a better understanding of the whole process.

## Javadoc

JRAW-Android uses JitPack to host its Javadoc.

```
https://jitpack.io/com/github/mattbdean/JRAW-Android/VERSION/javadoc/index.html
```

`VERSION` can be a specific commit hash (like [`9390529`](https://jitpack.io/com/github/mattbdean/JRAW-Android/9390529/javadoc/index.html)), a tag, or the HEAD of a branch (like [`master-SNAPSHOT`](https://jitpack.io/com/github/mattbdean/JRAW-Android/master-SNAPSHOT/javadoc/index.html)).

JitPack produces Javadoc only when necessary, so the first time someone accesses the Javadoc for a specific build it may take a little bit.

## FAQ

### How do I pass data around?

All JRAW models implement Serializable, so methods like [`Parcel.writeSerializable`](https://developer.android.com/reference/android/os/Parcel.html#writeSerializable(java.io.Serializable)) and [`Bundle.getSerializable`](https://developer.android.com/reference/android/os/Bundle.html#getParcelable(java.lang.String)) should work fine. You can also transform models to/from JSON if you're concerned about speed:

```java
// The serializeNulls() here is very important
JsonAdapter<Submission> adapter = JrawUtils.moshi.adapter(Submission.class).serializeNulls();
String json = adapter.toJson(someSubmission);

// Add the JSON to your Bundle/Parcel/whatever
bundle.putString("mySubmission", json);

// Later...
Submission pojo = adapter.fromJson(bundle.getString("mySubmission"));
someSubmission.equals(pojo); // => true
```

See [mattbdean/JRAW#221](https://github.com/mattbdean/JRAW/issues/221) for why the adapter needs to serialize nulls.

## Versioning

Unless otherwise noted, JRAW-Android's version is the same as JRAW. So JRAW-Android v1.1.0 would use JRAW v1.1.0.

## Contributing

This project uses Robolectric for unit tests. Linux and Mac users on Android Studio should see [this](http://robolectric.org/getting-started/#note-for-linux-and-mac-users) when running tests through the IDE.
