package norollbackfor

fun main() {
    val entity = ExampleEntity(name = "two", age = 2)
    val service = ExampleService()

    service.saveAndThrow(entity)
}