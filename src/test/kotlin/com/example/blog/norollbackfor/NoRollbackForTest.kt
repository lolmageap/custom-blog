package com.example.blog.norollbackfor

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NoRollbackForTest(
    private val exampleUseCase: ExampleUseCase,
    private val exampleRepository: ExampleRepository,
): StringSpec({
    "noRollbackFor이 붙어 있는 매서드 내에서 예외가 발생하면 롤백되지 않는다." {
        val entity = ExampleEntity("name", 10)
        shouldThrow<IllegalStateException> {
            exampleUseCase.noRollback(entity)
        }

        exampleRepository.findAll().size shouldBe 1
    }

    "noRollbackFor을 설정을 해도 noRollbackFor이 붙어 있는 매서드 밖에서 예외가 발생하면 롤백이 된다." {
        val entity = ExampleEntity("nickname", 20)
        shouldThrow<IllegalStateException> {
            exampleUseCase.rollback(entity)
        }

        exampleRepository.findAll().size shouldBe 1
    }

    beforeEach {
        exampleRepository.deleteAllInBatch()
    }
})