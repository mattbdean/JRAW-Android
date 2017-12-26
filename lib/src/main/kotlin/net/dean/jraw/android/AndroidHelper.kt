package net.dean.jraw.android

import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.NoopTokenStore
import net.dean.jraw.oauth.TokenStore
import okhttp3.OkHttpClient
import java.util.*

/**
 * A few JRAW utility methods specific to the Android platform. See
 * [here](https://mattbdean.gitbooks.io/jraw/content/oauth2.html#accounthelper) for more information.
 */
object AndroidHelper {
    /** Creates an AccountHelper */
    @JvmOverloads
    @JvmStatic
    fun accountHelper(clientId: String,
                      redirectUrl: String,
                      userAgent: UserAgent,
                      deviceUUID: UUID,
                      tokenStore: TokenStore = NoopTokenStore(),
                      http: OkHttpClient = OkHttpClient()): AccountHelper {

        val networkAdapter = OkHttpNetworkAdapter(userAgent, http)
        val creds = Credentials.installedApp(clientId, redirectUrl)

        return AccountHelper(networkAdapter, creds, tokenStore, deviceUUID)
    }

    /**
     * Creates an AccountHelper using an [AppInfoProvider] to specify OAuth2 app details.
     *
     * @see ManifestAppInfoProvider
     */
    @JvmOverloads
    @JvmStatic
    fun accountHelper(provider: AppInfoProvider,
                      deviceUUID: UUID,
                      tokenStore: TokenStore = NoopTokenStore(),
                      http: OkHttpClient = OkHttpClient()): AccountHelper {

        val meta = provider.provide()
        return accountHelper(meta.clientId, meta.redirectUrl, meta.userAgent, deviceUUID,
                tokenStore, http)
    }
}
