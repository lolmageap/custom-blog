package com.example.blog.checkedexception

import io.kotest.core.spec.style.StringSpec
import java.io.File

class Catch: StringSpec({
    "catch 함수 테스트" {
        { File("test.txt").copyTo(File("test2.txt")) }
            .catch {
                println(it.message)
            }
    }
})

inline fun <T> (() -> T).catch(block: (Exception) -> Unit): T? {
    return try {
        this()
    } catch (e: Exception) {
        block(e)
        null
    }
}
