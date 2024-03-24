package com.example.blog.checkedexception

import io.kotest.core.spec.style.StringSpec
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class CheckedExceptionV2: StringSpec({
    "test" {
        val file = File("test.txt")
        FileReader(file).use { fileReader ->
            val reader = BufferedReader(fileReader)
            reader.readLine()
        }
    }
})