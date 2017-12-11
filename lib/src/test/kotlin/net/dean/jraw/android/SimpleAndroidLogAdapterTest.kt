package net.dean.jraw.android

import android.util.Log
import com.winterbe.expekt.should
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class SimpleAndroidLogAdapterTest {
    val tag = "TESTING-TAG"

    @After
    fun tearDown() {
        ShadowLog.reset()
    }

    @Test
    fun shouldLogAtRequestedLevel() {
        val level = Log.INFO
        val msg = "foo"

        val adapter = SimpleAndroidLogAdapter(level, tag)
        adapter.writeln(msg)
        ShadowLog.getLogs().should.equal(listOf(ShadowLog.LogItem(level, tag, msg, null)))
    }

    @Test
    fun shouldNotLogWhenLevelNotLoggable() {
        val level = Log.VERBOSE
        val msg = "foo"
        val adapter = SimpleAndroidLogAdapter(level, tag)

        // By default, only INFO and above are loggable
        adapter.writeln(msg)
        ShadowLog.getLogs().should.be.empty

        // Use ShadowLog to explicitly allow this tag at this level to be logged
        ShadowLog.setLoggable(tag, level)
        adapter.writeln(msg)
        ShadowLog.getLogs().should.equal(listOf(ShadowLog.LogItem(level, tag, msg, null)))
    }
}
