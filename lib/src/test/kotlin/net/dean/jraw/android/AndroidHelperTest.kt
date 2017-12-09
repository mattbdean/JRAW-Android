package net.dean.jraw.android

import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.NoopTokenStore
import okhttp3.OkHttpClient
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class AndroidHelperTest {
    private val clientId = "<clientId>"
    private val redirectUrl = "<redirectURL>"
    private val userAgent = UserAgent("<userAgent>")
    private val deviceUUID = UUID.randomUUID()!!
    private val tokenStore = NoopTokenStore()
    private val http = OkHttpClient()

    @Test
    fun shouldUseManuallyProvidedDataToCreateAnAccountHelper() {
        AndroidHelper.accountHelper(clientId, redirectUrl, userAgent, deviceUUID, tokenStore, http)
    }

    @Test
    fun shouldUseAnAppInfoProviderWhenGiven() {
        AndroidHelper.accountHelper(object: AppInfoProvider {
            override fun provide(): AppInfo = AppInfo(clientId, redirectUrl, userAgent)
        }, deviceUUID, tokenStore, http)
    }
}
