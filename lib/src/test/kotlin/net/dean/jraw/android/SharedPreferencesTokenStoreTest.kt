package net.dean.jraw.android

import com.winterbe.expekt.should
import net.dean.jraw.JrawUtils
import net.dean.jraw.models.OAuthData
import net.dean.jraw.models.PersistedAuthData
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class SharedPreferencesTokenStoreTest {
    private lateinit var store: SharedPreferencesTokenStore
    private val adapter = JrawUtils.adapter<PersistedAuthData>()
    private val username = "foo"

    @Before
    fun setUp() {
        store = SharedPreferencesTokenStore(RuntimeEnvironment.application)
        store.sharedPreferences.edit().clear().apply()
    }

    @Test
    fun shouldLoadDataFromPrefs() {
        val username = "foo"
        val authData = mockAuthData()
        store.sharedPreferences.edit().putString(username, adapter.toJson(authData)).apply()

        store.load()
        store.usernames.should.equal(listOf(username))
        store.fetchLatest(username).should.equal(authData.latest)
        store.fetchRefreshToken(username).should.equal(authData.refreshToken)
    }

    @Test
    fun shouldSaveToPrefs() {
        val authData = mockAuthData()
        store.sharedPreferences.getString(username, null).should.be.`null`

        store.storeLatest(username, authData.latest!!)
        store.storeRefreshToken(username, authData.refreshToken!!)

        store.persist()

        adapter.fromJson(store.sharedPreferences.getString(username, "")).should.equal(authData)
    }

    private fun mockAuthData(): PersistedAuthData {
        val expiration = Date(Date().time + TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
        return PersistedAuthData.create(OAuthData.create("access_token", listOf("*"), "", expiration), "refresh_token")
    }

}
