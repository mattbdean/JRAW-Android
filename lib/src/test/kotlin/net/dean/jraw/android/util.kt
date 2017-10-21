package net.dean.jraw.android

inline fun <reified T : Exception> expectException(doWork: () -> Unit): T {
    val message = "Should have thrown ${T::class.java.name}"
    try {
        doWork()
        throw IllegalStateException(message)
    } catch (e: Exception) {
        // Make sure rethrow the Exception we created here
        if (e.message == message) throw e
        // Make sure we got the right kind of Exception
        if (e::class.java != T::class.java)
            throw IllegalStateException("Expecting function to throw ${T::class.java.name}, instead threw ${e::class.java.name}", e)
        return e as T
    }
}
