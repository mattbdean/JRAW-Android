package net.dean.jraw.android

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import net.dean.jraw.JrawUtils
import net.dean.jraw.models.PersistedAuthData
import net.dean.jraw.oauth.DeferredPersistentTokenStore

/**
 * This TokenStore implementation persists OAuthData and refresh tokens to a private
 * SharedPreferences.
 *
 * SharedPreferences are persisted using `apply()` rather than `commit()`, so it might be a good
 * idea to enable [autoPersist].
 *
 * Create an instance using [create].
 *
 * It should be noted that this is probably not the most efficient or secure means of storing this
 * kind of data. For apps that have several hundred users, it might be better to store this
 * information in a database.
 */
class SharedPreferencesTokenStore
    @Deprecated(
            message = "Use SharedPreferencesTokenStore.create()",
            replaceWith = ReplaceWith("SharedPreferencesTokenStore.create(context)")
    ) constructor(context: Context) : DeferredPersistentTokenStore() {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            context.getString(R.string.prefs_file), Context.MODE_PRIVATE)

    override fun doLoad(): Map<String, PersistedAuthData> {
        return sharedPreferences
                .all
                // Only operate on key-value pairs whose value is a string (since we store all data
                // as strings)
                .filterValues { it is String }
                // Parse the JSON value to a PersistedAuthData
                .map { (username, data) -> username to adapter.fromJson(data as String)!! }
                .toMap()
    }

    override fun doPersist(data: Map<String, PersistedAuthData>) {
        val editor = sharedPreferences.edit().clear()

        for ((username, persistedData) in data) {
            editor.putString(username, adapter.toJson(persistedData))
        }

        editor.apply()
    }

    companion object {
        private val adapter: JsonAdapter<PersistedAuthData> = JrawUtils.adapter()

        /**
         * Creates a SharedPreferencesTokenStore and loads the existing data. Also enables
         * [autoPersist].
         */
        @JvmStatic fun create(context: Context): SharedPreferencesTokenStore {
            @Suppress("DEPRECATION")
            val store = SharedPreferencesTokenStore(context)
            store.load()
            store.autoPersist = true
            return store
        }
    }
}

