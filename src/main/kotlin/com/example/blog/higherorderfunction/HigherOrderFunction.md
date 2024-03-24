# 고차 함수

고차 함수는 다른 함수를 인자로 받거나 함수를 반환하는 함수를 말합니다.  
kotlin에서는 함수를 일급 객체로 다루기 때문에 함수를 인자로 받거나 반환할 수 있습니다.  
아래 calculate 함수는 두 개의 정수를 받아서 함수를 실행하는 함수입니다.

```kotlin
fun main() {
    val function: KFunction = ::sum
    
    calculate(10, 20, function)
    calculate(10, 20, ::sum)
    calculate(10, 20) { a, b ->
        a * b
    }
}

fun calculate(a: Int, b: Int, function: (Int, Int) -> Int) {
    val result = function(a, b)
    println(result)
}

fun sum(a: Int, b: Int): Int {
    return a + b
}
```

multiply 함수는 두 개의 정수를 받아서 두 정수를 곱한 값을 반환하는 함수를 반환합니다.  
function 변수는 타입이 (Int) -> Int 입니다.  
result 변수는 invoke 메서드로 함수를 실행할 수 있습니다. (invoke 메서드는 operator overloading이 되어있기 떄문에 생략 가능합니다.)  
실행 할 때 인자로 2를 넣어주면 2 * 10 * 20 = 400 이 반환됩니다.

```kotlin
fun main() {
    val function: (Int) -> Int = multiply(10, 20)
    val result = function.invoke(2)

    require(result == 400)
}

fun multiply(a: Int, b: Int): (Int) -> Int {
    return { c ->
        c * a * b
    }
}
```

## JoinToString
kotlin의 joinToString 함수는 컬렉션의 요소를 문자열로 변환하는 함수입니다.  
joinToString 함수는 Generic 타입을 받아서 문자열로 변환합니다.  
먼저 Parents와 Children 데이터 클래스를 정의합니다.
```kotlin
data class Parents(
    val name: String,
    val age: Int,
    val children: List<Children>
)

data class Child(
    val name: String,
    val age: Int,
)

val child1 = Child("child1", 10)
val child2 = Child("child2", 20)
val child3 = Child("child3", 30)

val parents = Parents("parent", 40, listOf(child1, child2, child3))

fun main() {
    val childrenNames = parents.children.joinToString(
        separator = ", ",
        prefix = "[",
        postfix = "]",
    ) { it.name }
    
    require(childrenNames == "[child1, child2, child3]")
}
```

kotlin은 joinToString 함수 내부를 어떻게 구현했는지 확인해보겠습니다.
```kotlin
fun <T> Iterable<T>.joinToStringCustom(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): String {
    return joinToCustom(
        StringBuilder(),
        separator,
        prefix,
        postfix,
        limit,
        truncated,
        transform,
    ).toString()
}

fun <T, A : Appendable> Iterable<T>.joinToCustom(
    buffer: A,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): A {
    buffer.append(prefix)
    var count = 0
    for (element in this) {
        if (++count > 1) buffer.append(separator)

        // 0보다 작으면 limit 없이 배열의 size 만큼 실행, count 가 limit 보다 크면 break
        if (limit < 0 || count <= limit) {
            buffer.appendElement(element, transform)
        } else break
    }
    
    // limit 이 0보다 크고 count 가 limit 보다 크면 truncated 추가
    if (limit in 0 until count) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}

fun <T> Appendable.appendElement(element: T, transform: ((T) -> CharSequence)?) {
    when {
        transform != null -> append(
            transform(element)
        )
        element is CharSequence? -> append(element)
        element is Char -> append(element)
        else -> append(element.toString())
    }
}
```

### 분석 
 - 먼저 joinToString은 transform이라는 함수를 매개변수로 받습니다.
```kotlin
// transform: (T) -> CharSequence
```

 - 위에서 작성했던 JoinToStringCustom 코드를 조금 더 상세하게 풀어보겠습니다.  
타입 T를 받아서 CharSequence로 변환하는 함수입니다.
```kotlin
/** List의 Generic type 은 현재 Child 입니다.
 *  transform 함수의 매개변수 타입은 Child 입니다. ( <T> == it == Child )
 *  transform 함수는 Child를 받아서 CharSequence로 변환해야합니다.
 *  따라서 반환 타입은 CharSequence 타입이어야하며 함수의 scope가 닫힐 때 마지막줄은 CharSequence여야 합니다.
 *  따라서 it.name 은 CharSequence 타입이기 때문에 컴파일 에러가 발생하지 않습니다.
 */
List<Child>.joinToStringCustom {
    it.name
}
```

 - 함수 인자 안에 다른 함수를 넣어서 사용할 수 있습니다.
```kotlin
fun main() {
    val childrenNames = parents.children.joinToStringCustom { child ->
        println(child) // <-- 추가
        child.age.toString()
    }
}
```

 - 그러면 함수의 인자가 2개가 될때 어떻게 될까요?  
인자가 2개일 땐 함수 스코프 내부에 Parameter Labeling을 필수로 해줘야합니다.  
아래 함수는 Iterable 타입의 T와 Int를 받아서 CharSequence로 변환하는 함수입니다.
```kotlin
// kotlin은 구조 분해 할당도 지원해 주기 떄문에 (index, element)로 구조 분해 할당을 하여 index와 element를 사용할 수 있습니다.
fun <T> Iterable<T>.joinToStringCustomV2(
    transform: (T, Int) -> CharSequence,
): String {
    return StringBuilder().let { builder ->
        for ((index, element) in this.withIndex()) {
            builder.appendLine(
                transform(element, index)
            )
        }
        
        builder.trim().toString()
    }
}

fun main() {
    val parents = Parents(
        name = "parent",
        age = 40,
        children = listOf(
            Child("child1", 10),
            Child("child2", 20),
            Child("child3", 30),
        )
    )

    val result = parents.children.joinToStringCustomV2 { element, index ->
        "${index + 1}: ${element.name}"
    }

    require(result == """
            1: child1
            2: child2
            3: child3
        """.trimIndent())
}
```