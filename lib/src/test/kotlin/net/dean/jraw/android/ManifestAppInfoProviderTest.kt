package net.dean.jraw.android

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.winterbe.expekt.should
import net.dean.jraw.http.UserAgent
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(25))
class ManifestAppInfoProviderTest {
    private val mockClientId = "<client ID>"
    private val mockRedirectUrl = "<redirect URL>"
    private val mockUsername = "<reddit username>"

    private fun init(includeCreds: Boolean, initBundle: (empty: Bundle) -> Unit = {}): ManifestAppInfoProvider {
        val b = Bundle()
        if (includeCreds) {
            b.putString(ManifestAppInfoProvider.KEY_CLIENT_ID, mockClientId)
            b.putString(ManifestAppInfoProvider.KEY_REDIRECT_URL, mockRedirectUrl)
        }

        initBundle(b)

        val info = ApplicationInfo()
        info.metaData = b

        // Mock the call to context.getPackageManager().getApplicationInfo(...). There's probably a
        // better way to do this.
        val mockPm = mock<PackageManager> {
            on { getApplicationInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_META_DATA) }
                    .doReturn(info)
        }

        val mockContext = mock<Context> {
            on { packageName } doReturn BuildConfig.APPLICATION_ID
            on { packageManager } doReturn mockPm
        }

        return ManifestAppInfoProvider(mockContext)
    }

    @Test
    fun shouldWorkWithOverrideKey() {
        val provider = init(includeCreds = true) {
            it.putString(ManifestAppInfoProvider.KEY_REDDIT_USERNAME, mockUsername)
        }

        provider.provide().should.equal(AppInfo(mockClientId, mockRedirectUrl,
                UserAgent("android", BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, mockUsername)))
    }

    @Test
    fun shouldWorkWithUsernameKey() {
        val someUserAgent = "<user agent override>"
        val provider = init(includeCreds = true) {
            it.putString(ManifestAppInfoProvider.KEY_USER_AGENT_OVERRIDE, someUserAgent)
        }

        provider.provide().should.equal(AppInfo(mockClientId, mockRedirectUrl, UserAgent(someUserAgent)))
    }

    @Test
    fun shouldRequireUserAgent() {
        val provider = init(includeCreds = true)

        expectException<IllegalStateException> { provider.provide() }.message.should.contain("UserAgent")
    }

    @Test
    fun shouldRequireClientId() {
        val provider = init(includeCreds = false) {
            it.putString(ManifestAppInfoProvider.KEY_USER_AGENT_OVERRIDE, "<user agent override>")
            it.putString(ManifestAppInfoProvider.KEY_REDIRECT_URL, mockRedirectUrl)
        }

        expectException<IllegalStateException> { provider.provide() }.message.should.contain("client ID")
    }

    @Test
    fun shouldRequireRedirectUrl() {
        val provider = init(includeCreds = false) {
            it.putString(ManifestAppInfoProvider.KEY_CLIENT_ID, mockClientId)
            it.putString(ManifestAppInfoProvider.KEY_REDDIT_USERNAME, mockUsername)
        }

        expectException<IllegalStateException> { provider.provide() }.message.should.contain("redirect URL")
    }
}
