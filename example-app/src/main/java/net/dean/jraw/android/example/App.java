package net.dean.jraw.android.example;

import android.app.Application;

import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.oauth.AccountHelper;

import java.util.UUID;

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

        accountHelper = AndroidHelper.accountHelper(provider, deviceUuid, tokenStore);
    }

    public static AccountHelper getAccountHelper() { return accountHelper; }
    public static SharedPreferencesTokenStore getTokenStore() { return tokenStore; }
}