package checkedexception

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun main() {
    val file = File("test.txt")
    FileReader(file).use { fileReader ->
        val reader = BufferedReader(fileReader)
        reader.readLine()
    }
}