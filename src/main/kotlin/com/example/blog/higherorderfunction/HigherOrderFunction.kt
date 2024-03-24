package com.example.blog.higherorderfunction

fun main() {
    calculate(10, 20, ::sum)
    calculate(10, 20) { a, b -> a * b }
}

fun calculate(a: Int, b: Int, function: (Int, Int) -> Int) {
    val result = function(a, b)
    println(result)
}

fun sum(a: Int, b: Int): Int {
    return a + b
}