package com.example.blog.checkedexception

import io.kotest.core.spec.style.StringSpec
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class CheckedExceptionV1: StringSpec({
    "test" {
        val file = File("test.txt")
        val fileReader = FileReader(file)
        val reader = BufferedReader(fileReader)
        reader.readLine()
    }
})