# JRAW-Android

This is an extension to the [Java Reddit API Wrapper](https://github.com/thatJavaNerd/JRAW) that adds some Android-specific classes.

#### Getting Started

Getting started is simple. Add this to your application's `onCreate`:

```java
// Android-specific RedditClient
RedditClient reddit = new AndroidRedditClient(this);

// Store refresh tokens in SharedPreferences
RefreshTokenHandler handler = new RefreshTokenHandler(new AndroidTokenStore(this), reddit);

// Initialize the AuthenticationManager singleton
AuthenticationManager.get().init(reddit, handler);
```

AndroidRedditClient can automagically deduce a User-Agent for your app. Add one of these <meta-data> tags to your `AndroidManifest.xml`:

```xml
<application>
    ...
    <meta-data android:name="net.dean.jraw.REDDIT_USERNAME" android:value="(your reddit username)" />
    <!-- OR -->
    <meta-data android:name="net.dean.jraw.USER_AGENT_OVERRIDE" android:value="(custom User-Agent)" />
</application>
```
