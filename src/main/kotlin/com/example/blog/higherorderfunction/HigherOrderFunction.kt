package com.example.blog.higherorderfunction

fun main() {
    calculate(10, 20, ::sum)
    calculate(10, 20) { a, b -> a * b }

    val function: (Int) -> Int = multiply(10, 20)

    val result = function.invoke(2)
    require(result == 400)

    val list = listOf(1, 2, 3)

    list.ifNotEmpty { println(this) }
    list.ifDistinct { println(it) }
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

inline fun <T> Collection<T>.ifNotEmpty(
    transform: T.() -> Unit,
) {
    if (this.isNotEmpty()) {
        this.forEach {
            it.transform()
        }
    }
}

inline fun <T> Collection<T>.ifDistinct(
    transform: (T) -> Unit,
) {
    if (this.distinct().size == this.size) {
        this.forEach {
            transform(it)
        }
    }
}