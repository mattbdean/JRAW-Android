package net.dean.jraw.android.example;

import android.app.Application;

import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.ManifestAppInfoProvider;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.TokenStore;

import java.util.UUID;

public final class App extends Application {
    private static AccountHelper accountHelper;

    @Override
    public void onCreate() {
        super.onCreate();

//        AppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());
//        UUID deviceUuid = UUID.randomUUID();
//        TokenStore tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
//
//        accountHelper = AndroidHelper.accountHelper(provider, deviceUuid, tokenStore);
    }

    public static AccountHelper getAccountHelper() { return accountHelper; }
}