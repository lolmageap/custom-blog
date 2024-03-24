package com.example.blog.higherorderfunction

fun main() {
    calculate(10, 20, ::sum)
    calculate(10, 20) { a, b -> a * b }

    val function: (Int) -> Int = multiply(10, 20)

    val result = function.invoke(2)
    require(result == 400)
}

fun calculate(a: Int, b: Int, function: (Int, Int) -> Int) {
    val result = function(a, b)
    println(result)
}

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun multiply(a: Int, b: Int): (Int) -> Int {
    return { c ->
        c * a * b
    }
}