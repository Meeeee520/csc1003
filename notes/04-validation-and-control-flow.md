# 04 整数判断、异常处理与循环

## 两种“整数”问题

1. 判断已经读到的数在数学上是否为整数，例如 double 值 3.0。
2. 判断输入能否被 Scanner 读成 int，例如文本 3。

`hasNextInt()` / `nextInt()` 针对第二种。输入 `3.0` 虽然数学上是整数，但不是 nextInt 接受的整数格式。

对于有限 double，可以用 `Double.isFinite(x) && x % 1 == 0` 判断其存储值有无小数部分。
`x == (int) x` 只适合 int 范围内的值；强转不是通用整数检查，浮点计算误差也需要单独考虑。

## 读取 1–100 的整数

这是项目中反复讨论的写法；非整数格式靠异常处理，范围靠 if 判断。

```java
import java.util.InputMismatchException;
import java.util.Scanner;

public class ReadInteger {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a;

        while (true) {
            try {
                a = sc.nextInt();
                if (a >= 1 && a <= 100) {
                    break;
                }
            } catch (InputMismatchException e) {
                sc.next(); // 消费错误 token，避免下次再次读到它
            }
            System.out.println("Invalid input");
        }
        System.out.println(a);
    }
}
```

此例假定输入流会继续提供数据。EOF（输入结束）是另一种情况，不能用 InputMismatchException 处理。

不要在 try 开头无条件打印 Invalid input，否则正确输入也会收到报错。

## sc.next() 到底在做什么？

输入 `abc` 时，nextInt 失败，abc 仍留在输入中。catch 里的 `sc.next()` 把这个 token 读走并丢弃；它没有把 abc 转成整数。

| 恢复方式 | 消费内容 | 适用情况 |
| --- | --- | --- |
| `sc.next()` | 一个错误 token | 每个 token 分别处理 |
| `sc.nextLine()` | 当前行剩余内容 | 错误后丢弃整行重新输入 |

例如一行是 `abc 20`：next 只清除 abc，下一次可能读到 20；nextLine 会连同 20 一起丢弃。
选择哪一种取决于题目按 token 还是按整行输入。

### nextInt 不会跳过非法 token

`nextInt()` 会跳过空白去查看下一个 token，但不会跳过一个无法解析为整数的非空白 token。遇到 `abc` 时，它在 `abc` 处失败并抛异常；必须由 catch 中的 `next()` 或 `nextLine()` 清理输入。

```java
Scanner sc = new Scanner("abc 25\n40\n");
try {
    sc.nextInt();       // 在 abc 处失败；没有读到 25 或 40
} catch (InputMismatchException e) {
    sc.nextLine();      // 丢弃 abc、25 和本行换行
}
int value = sc.nextInt(); // 读取 40
```

注意：`System.out.println(sc.nextInt())` 仍然只调用一次 `nextInt()`。这一次调用先读取并返回整数，`println` 再打印这个返回值。

## 不使用异常的写法

```java
while (sc.hasNext()) {
    if (!sc.hasNextInt()) {
        sc.next();
        System.out.println("Invalid input");
        continue;
    }
    int a = sc.nextInt();
    if (a >= 1 && a <= 100) {
        break;
    }
    System.out.println("Invalid input");
}
```

此段演示判断流程；a 的作用域只在循环体内。如果循环外需要 a，应在循环前声明，并考虑输入结束却未获得有效值的情况。

## `Exception` 和 `IOException` 怎么选？

`Exception` 是许多异常的父类，范围很大；`IOException` 是它的子类，专门表示读取或写入文件、流、网络数据时发生的输入输出错误。

原则是：**捕获自己能够处理的最具体异常**。文件读取 API 声明会抛出 `IOException` 时，就捕获 `IOException`；不要因为不清楚错误类型就一律写 `catch (Exception e)`，否则可能把程序中的其他错误也一起隐藏。

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFile {
    public static void main(String[] args) {
        try {
            String text = Files.readString(Path.of("data.txt"));
            System.out.println(text);
        } catch (IOException e) {
            System.out.println("文件读取失败：" + e.getMessage());
        }
    }
}
```

### `catch` 还是 `throws`？

- 当前方法知道怎样恢复或提示用户：使用 `catch` 处理。
- 当前方法无法合理处理，想交给调用者决定：在方法声明中写 `throws IOException`。
- `throws` 只是把处理责任向上传递，并不会让异常消失。

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveFile {
    static void save(String text) throws IOException {
        Files.writeString(Path.of("data.txt"), text);
    }

    public static void main(String[] args) {
        try {
            save("hello");
        } catch (IOException e) {
            System.out.println("保存失败：" + e.getMessage());
        }
    }
}
```

这里 `save` 不处理文件写入错误，而是用 `throws IOException` 交给调用它的 `main`；`main` 再用 `catch` 真正处理。`IOException` 属于受检异常，调用者必须继续声明 `throws` 或使用 `try-catch`。

### 可以用 `throws Exception` 解决所有错误吗？

语法上可以把方法声明写成 `throws Exception`。因为 `IOException`、`NumberFormatException` 等都是 `Exception` 的子类，这个声明允许这些异常继续传给调用者：

```java
static void load() throws Exception {
    // 方法中的 Exception 子类可以继续向上传递
}
```

但它不等于“解决了所有错误”：

- `throws` 只是声明并传递异常，没有真正处理异常。
- `Exception` 不包含 `Error`，例如 `OutOfMemoryError`、`StackOverflowError`。
- 拼写、类型不匹配等编译错误也不是运行时的 `Exception`，不能靠 `throws Exception` 修复。
- 声明过宽会让调用者不知道真正可能发生什么，也可能掩盖设计问题。

因此，文件读写明确可能抛出 `IOException` 时，优先写 `throws IOException`；只有方法确实可能传出多种无法在此处理的受检异常时，才考虑更宽的声明。

### `Exception` 后面的 `e` 是什么？

准确的类名是 `Exception` 和 `IOException`，类名本身后面都没有额外的 `e`。Java 区分大小写：

```java
void load() throws IOException { }

try {
    load();
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

- `throws IOException`：只写异常类型，不写变量名。
- `catch (IOException e)`：`IOException` 是类型，`e` 是接住异常对象的变量名。
- 变量名可以改成 `error`，例如 `catch (IOException error)`。
- 正确大小写是 `Exception`、`IOException`，不是 `exception` 或 `Ioexception`。

记忆：**`throws` 后面写类型；`catch` 括号中写“类型 + 变量名”。**

### `throw` 和 `throws` 不是一回事

- `throws IOException` 写在方法声明中，说明方法**可能**把这种异常交给调用者。
- `throw new IllegalArgumentException("...")` 写在方法体中，表示现在**主动抛出一个具体异常对象**。

```java
static void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("age 不能为负数");
    }
}
```

### 易错点

- `InputMismatchException` 表示 Scanner 读到的 token 格式不匹配，不属于 `IOException`。
- 同一个 try 有多个 catch 时，子类要写在父类前面；`catch (Exception e)` 放在 `catch (IOException e)` 前面会导致后者无法到达，编译失败。
- 只在程序最外层确实需要统一记录或终止时才考虑捕获 `Exception`，并且不要悄悄忽略异常。

## break、continue 与循环层级

- `break`：结束最近的一层循环。
- `continue`：跳过本轮剩余代码，进入最近一层循环的下一轮。
- for 循环中 continue 后仍执行更新表达式，再判断条件。
- 嵌套循环里的 break 不会自动结束所有循环。
- 有效输入计数只在成功时增加，比先增加再 k-- 更容易理解。
