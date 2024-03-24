# Checked Exception
kotlin 은 java 와 다르게 checked exception 을 "강제" 하지 않습니다.

checked exception 을 사용하면 호출 하는 쪽에서 반드시 예외 처리를 해줘야하고 코드를 읽기 어렵게 만들고 가독성을 떨어뜨립니다.

그래서 kotlin 은 checked exception 을 사용하지 않고 unchecked exception 만 사용합니다.

아래는 FileReader 객체입니다. FileReader를 생성 할 때 무조건 FileNotFoundException 를 호출하는 쪽으로 예외를 미룹니다.

```java
    public FileReader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
    }
```

kotlin은 동일하게 FileReader 객체를 사용하지만 호출하는 쪽으로 예외를 미뤄도 컴파일 에러가 발생시키지 않습니다.

필요에 따라 Throws annotation을 사용하여 예외를 미루는 것은 가능하며 물론 예외 처리를 강제하지는 않습니다.

```kotlin
@Throws(FileNotFoundException::class)
```

## 단점
checked exception 을 사용하지 않는 것은 가독성을 좋게 해주는 장점이 있지만 단점도 있습니다.

checked exception 을 사용하지 않으면 예외 처리를 강제하지 않기 때문에 개발자가 예외 처리를 하지 않을 수 있습니다.

예를 들어 IOException 같은 경우 개발자가 예외 처리를 하지 않으면 메모리 누수가 발생하여 application이 종료 되는 문제가 생길 수 있습니다.

### 예제

#### java
아래는 java 에서 checked exception 을 사용한 예제 입니다.

java 는 checked exception 을 강제하며 try-with-resources 를 사용해 구현 했습니다.

try 함수에 Closeable interface implement 객체를 넣으면 try block 이 끝나면서 자동으로 Closable.close() 함수를 호출하여 자원을 해제합니다.

```java
public class CheckedException {
    public static void main(String[] args) {
        File file = new File("test.txt");

        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader reader = new BufferedReader(fileReader);
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

#### kotlin
아래는 kotlin 에서 checked exception 을 사용한 예제 입니다.

```kotlin
fun main() {
    val file = File("test.txt")
    val fileReader = FileReader(file)
    val reader = BufferedReader(fileReader)
    reader.readLine()
}
```

kotlin 은 checked exception 을 강제하지 않습니다.

따라서 개발자가 예외 처리를 하지 않아도 컴파일 에러가 발생하지 않습니다.

파일 입출력을 할 떄 예외 처리를 하지 않고 예외가 발생하게 되었다고 가정하겠습니다.

1. File I/O는 운영체제에 의해 관리되는 자원이라 application은 사용중인지 사용하지 않는지 파악하기 어렵습니다.
2. File I/O 작업 시 Closeable.close() 함수를 호출하지 않으면 os와 application은 파일 자원을 어플리케이션 종료시까지 공유하게 되며 파일에서 읽어온 데이터를 계속 메모리에 가지고 있게 됩니다.
3. 즉 파일 데이터는 계속 사용되는 것으로 판단이 되어 GC의 대상이 되지 않고 영원히 메모리에 남아있게 됩니다.
4. 이러한 문제로 인해 메모리 누수가 발생하여 application이 종료 될 수 있습니다.

#### 해결
kotlin은 이러한 문제를 해결하기 위해 Closeable.use 라는 확장 함수를 지원해줍니다.

java 의 try-with-resources 와 같은 역할을 하여 use block이 끝나면 자동으로 Closeable.close() 함수를 호출하여 자원을 해제해줍니다. 

use 함수는 try-with-resources 보다 간결하기 떄문에 kotlin의 의도인 간결한 코드를 작성하는데 도움을 줍니다.
```kotlin
fun main() {
    val file = File("test.txt")
    FileReader(file).use { fileReader ->
        val reader = BufferedReader(fileReader)
        reader.readLine()
    }
}
```

## 결론
kotlin을 사용하는 개발자라면 checked exception에 대한 이슈를 알고 있어야 합니다.