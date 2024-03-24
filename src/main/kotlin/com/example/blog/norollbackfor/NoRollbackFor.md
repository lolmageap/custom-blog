# NoRollbackFor

## NoRollbackFor 이란?

application을 개발하다 보면 특정 상황에 롤백을 시키면 안될 때가 존재합니다.

그런 상황에서 사용할 수 있는게 Transactional annotation의 noRollbackFor 입니다.

```kotlin
@Transactional(noRollbackFor = [Exception::class])
```

## 예시

하지만 사용을 하다 보면 noRollbackFor 이 동작 되지 않는 상황이 존재합니다.

아래는 예시 코드입니다.

```kotlin

```

```kotlin

```