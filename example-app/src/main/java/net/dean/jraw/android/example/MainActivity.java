package net.dean.jraw.android.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import net.dean.jraw.android.AppInfo;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.ManifestAppInfoProvider;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        AppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());
        AppInfo info = provider.provide();

        ((TextView) findViewById(R.id.clientId)).setText(info.getClientId());
        ((TextView) findViewById(R.id.redirectUrl)).setText(info.getRedirectUrl());
        ((TextView) findViewById(R.id.userAgent)).setText(info.getUserAgent().getValue());
    }
}
