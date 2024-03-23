package checkedexception

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun main() {
    val file = File("test.txt")
    val fileReader = FileReader(file)
    val reader = BufferedReader(fileReader)
    reader.readLine()
}