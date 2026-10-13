# 09 包与 import

## 概念：为什么需要 `import`

Java 用“包”（package）给类分组，避免大量类全部挤在同一个命名空间中。例如，`Scanner` 的完整类名是 `java.util.Scanner`：

- `java.util` 是包名；
- `Scanner` 是类名；
- `java.util.Scanner` 是全限定名。

`import` 的作用是告诉编译器：后面的短类名来自哪个包。它不会创建对象，也不是把整个包的代码复制到当前文件中。

```java
import java.util.Scanner;

public class ReadNumber {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int number = sc.nextInt();
        System.out.println(number);
        sc.close();
    }
}
```

导入后可以直接写 `Scanner`。如果不导入，也可以每次写全限定名：

```java
java.util.Scanner sc = new java.util.Scanner(System.in);
```

## 单类导入与通配符导入

```java
import java.util.Scanner; // 只导入 Scanner
import java.io.*;         // 可使用 java.io 包中的直接类
```

`java.io.*` 常用于文件输入输出，其中包括 `File`、`FileReader`、`FileWriter`、`BufferedReader`、`BufferedWriter` 和 `IOException` 等类。

通配符 `*` 只覆盖这个包中的直接类，不会继续导入子包。例如：

```java
import java.util.*;
```

这不等于导入 `java.util` 的所有子包。

初学时推荐明确写出实际使用的类，例如 `import java.util.Scanner;`。这样更容易看出程序依赖了什么，也能减少同名类带来的困惑。

## `java.lang` 为什么不用导入

`String`、`System`、`Math` 等常用类属于 `java.lang`。Java 会自动导入 `java.lang.*`，因此下面的代码不需要额外写 `import`：

```java
String text = "Java";
System.out.println(Math.max(3, 5));
```

## `import` 应该写在哪里

文件中的通常顺序是：

1. `package` 声明（如果有）；
2. `import` 语句；
3. 类声明。

```java
package demo;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.close();
    }
}
```

每条 `import` 语句末尾都要写分号。

## 同名类冲突

不同包里可能存在同名类。如果两个同名类同时使用，短类名可能无法确定具体指哪一个。这时可以对其中一个类使用全限定名：

```java
java.util.Date utilDate = new java.util.Date();
```

## 易错点

- `import` 只是让类名更方便使用，不会替你创建对象。
- `import java.util.*;` 不会导入 `java.util` 的子包。
- `String`、`System`、`Math` 属于自动导入的 `java.lang`，通常不用手动导入。
- `import` 应写在类声明之前；存在 `package` 声明时，应写在它之后。
- 每条 `import` 语句末尾都要有 `;`。
- 同一个包中的类可以直接互相使用，不需要导入。
- 明确导入具体类通常比大量使用 `*` 更容易阅读和排错。
