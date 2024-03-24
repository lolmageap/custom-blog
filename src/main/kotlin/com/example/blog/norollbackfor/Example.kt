package com.example.blog.norollbackfor

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@Entity
class ExampleEntity(
    @Id
    val name: String,
    val age: Int,
)

@Component
@Transactional
class ExampleUseCase(
    private val exampleService: ExampleService,
) {
    fun rollback(exampleEntity: ExampleEntity) {
        exampleService.save(exampleEntity)
        throw IllegalStateException("error")
    }

    fun noRollback(exampleEntity: ExampleEntity) {
        exampleService.saveAndThrow(exampleEntity)
    }
}

@Service
class ExampleService(
    private val exampleRepository: ExampleRepository,
) {
    @Transactional(noRollbackFor = [IllegalStateException::class])
    fun save(exampleEntity: ExampleEntity) {
        exampleRepository.save(exampleEntity)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = [IllegalStateException::class])
    fun saveAndThrow(exampleEntity: ExampleEntity) {
        exampleRepository.save(exampleEntity)
        throw IllegalStateException("error")
    }
}

interface ExampleRepository: JpaRepository<ExampleEntity, Long>
