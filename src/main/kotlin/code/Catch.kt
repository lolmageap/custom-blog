package code

import java.io.File

fun main() {
    { File("test.txt").copyTo(File("test2.txt")) }
        .catch {
            println(it.message)
        }
}

inline fun <T> (() -> T).catch(block: (Exception) -> Unit): T? {
    return try {
        this()
    } catch (e: Exception) {
        block(e)
        null
    }
}