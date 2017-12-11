package net.dean.jraw.android

import android.util.Log
import net.dean.jraw.http.LogAdapter

/**
 * Uses android.util.Log to log messages.
 */
class SimpleAndroidLogAdapter @JvmOverloads constructor(
        /** The priority of each message to log. Must be one of the android.util.Log constants. */
        var level: Int = Log.VERBOSE,

        /** The tag to use when logging. Default is "JRAW" */
        var tag: String = "JRAW"
) : LogAdapter {
    override fun writeln(data: String) {
        if (Log.isLoggable(tag, level))
            Log.println(level, tag, data)
    }
}