package norollbackfor

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

open class ExampleService {

    private lateinit var exampleRepository: ExampleRepository

    @Transactional(noRollbackFor = [Exception::class])
    open fun save(exampleEntity: ExampleEntity) {
        exampleRepository.save(exampleEntity)
    }

    @Transactional(noRollbackFor = [Exception::class])
    open fun saveAndThrow(exampleEntity: ExampleEntity) {
        exampleRepository.save(exampleEntity)
        throw IllegalStateException("error")
    }
}

interface ExampleRepository: JpaRepository<ExampleEntity, Long>

@Entity
class ExampleEntity(
    @Id
    val name: String,
    val age: Int,
)