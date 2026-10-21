# 01 输入输出、Scanner 与 Reader

## 程序的基本结构

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        System.out.println(a);
    }
}
```

- `import` 导入工具类；注意拼写，不能写成 `mport`。
- `new Scanner(System.in)` 创建读取键盘输入的对象，`sc` 是自己取的变量名。
- Java 声明变量需要类型，例如 `int a;`、`String name;`。赋值时不用重复声明：`a = 10;`。
- 局部变量使用前必须赋值。数组元素的默认值与局部变量不同。
- public 类名 Main 对应文件名 Main.java。语句通常以分号结束，代码块用大括号。

## 标准输入与 System.in

**Standard input（标准输入）**是程序读取数据的默认输入流。在终端中运行程序时，它通常连接键盘；Java 用 `System.in` 表示这个输入流。

```java
Scanner sc = new Scanner(System.in);
int age = sc.nextInt();
```

这里各部分的职责不同：

- `System.in`：数据来源，即标准输入流。
- `Scanner`：把输入流包装成更方便的读取工具。
- `nextInt()`：让 Scanner 从当前位置读取并解析一个整数。

标准输入不一定永远来自键盘。例如运行 `java Main < data.txt` 时，程序仍从 `System.in` 读取，但数据被操作系统从文件重定向进来。

与它对应的还有：

- `System.out`：标准输出，通常显示在终端。
- `System.err`：标准错误输出，通常也显示在终端，但可与普通输出分开重定向。

易错点：

- `System.in` 是字段，不是方法，不能写成 `System.in()`。
- `new Scanner(System.in)` 没有把 `System.in` 变成 Scanner；它是创建一个 Scanner，内部仍从标准输入流取数据。
- “标准输入通常是键盘”和“标准输入只能是键盘”不同；重定向后它可以来自文件或其他输入源。

## 常用输入方法

| 方法 | 读取内容 | 注意 |
| --- | --- | --- |
| `nextInt()` | 一个 int 整数 | 不接受 `3.0`；超出 int 范围也会失败 |
| `nextDouble()` | 一个浮点数 | 可以读 `3` 或 `3.5` |
| `next()` | 一个非空白片段（token） | 跳过开头空白，遇空格或换行停止 |
| `nextLine()` | 从当前位置到本行末尾 | 保留行内空格，不包含换行符 |
| `hasNextInt()` | 下一个 token 是否能读成 int | 只检查，不消费输入 |

## nextInt() 后为什么 nextLine() 像是被跳过？

输入 `12` 并回车后，`nextInt()` 读走 12，行尾仍在输入流中。紧接着 `nextLine()` 读取剩余部分，常得到空字符串。

```java
int age = sc.nextInt();
sc.nextLine();             // 读取本行剩余内容，走到下一行
String name = sc.nextLine();
```

这适用于“整数单独占一行，姓名在下一行”。若输入为 `12 apple`，读完 12 后 `nextLine()` 得到的是 `" apple"`；直接丢弃它也会丢掉 apple。

## Scanner 的 token、行和读取位置

可以把 Scanner 想成有一个“光标”，每次读取都会从当前位置开始。

- `nextInt()` 先查看下一个 token；能解析为 int 才消费它。
- 如果下一个 token 是 `abc`，`nextInt()` 抛出 `InputMismatchException`，而且不消费 `abc`。
- `next()` 消费一个 token。
- `nextLine()` 消费当前位置到本行末尾的全部内容，并越过该行的换行符。
- 这些方法不会凭空多读一次；每出现一次方法调用，才进行一次读取尝试。

逐步追踪：

```java
Scanner sc = new Scanner("oops 25\n40\n");

try {
    sc.nextInt();
} catch (InputMismatchException e) {
    sc.nextLine();
}
System.out.println(sc.nextInt());
```

1. try 中的 `nextInt()` 看见 `oops`，读取失败并抛异常；它没有读到 25 或 40。
2. catch 中的 `nextLine()` 丢弃当前行剩余的 `oops 25`，并越过第一个换行。
3. `System.out.println(sc.nextInt())` 里的 `nextInt()` 才读取下一行的 40。
4. 这个 40 同时作为 `println` 的参数，所以打印 40；并不存在额外的一次读取。

## `FileReader` 读取的是文件，还是字符串？

### 概念

`FileReader` 的构造器可以接收一个 `String`，但这个字符串表示**文件名或文件路径**，不是要读取的文本内容。

```java
String f = "data.txt";
FileReader reader = new FileReader(f);
```

上面的代码会尝试打开名为 `data.txt` 的文件。变量 `f` 只是保存路径；`FileReader` 随后从这个文件读取字符。如果文件不存在，会抛出 `FileNotFoundException`。

如果要把内存中已有的字符串当作字符流读取，应使用 `StringReader`：

```java
String text = "ABC\nDEF";
StringReader reader = new StringReader(text);
```

二者都属于字符输入体系，可以交给 `BufferedReader` 包装，再使用 `readLine()` 按行读取：

| 写法 | 真正的数据来源 |
| --- | --- |
| `new FileReader("data.txt")` | 磁盘中的 `data.txt` 文件 |
| `new StringReader("data.txt")` | 字符串本身的八个字符 `data.txt` |
| `new BufferedReader(reader)` | 不改变数据来源，只增加按行读取等功能 |

### 示例

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class ReaderDemo {
    static void showFile(String fileName) throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String text = "ABC\nDEF";
        try (BufferedReader br =
                     new BufferedReader(new StringReader(text))) {
            System.out.println(br.readLine()); // ABC
            System.out.println(br.readLine()); // DEF
            System.out.println(br.readLine()); // null：已经读到末尾
        }
    }
}
```

`BufferedReader.readLine()` 从当前数据源读取一行，并把这一行作为 `String` 返回。返回类型是 `String`，不代表数据源一定是字符串；数据源仍由里面包装的 `FileReader` 或 `StringReader` 决定。

### 易错点

- `new FileReader(f)` 不会读取变量 `f` 保存的文本内容，而会把它解释为文件路径。
- `BufferedReader` 是包装器，不会把文件自动变成字符串来源。
- `readLine()` 遇到输入末尾返回 `null`；读到真正的空行时返回 `""`，二者不同。
- `readLine()` 返回的字符串不包含行尾换行符。
- 文件读取可能抛出 `IOException`，应使用 `try-catch` 处理，或在方法上声明 `throws IOException`。

## 输出方法

```java
System.out.print("Hello");       // 不自动换行
System.out.println("Hello");     // 输出后换行
System.out.printf("%.2f%n", 3.5); // 3.50，然后换行
System.err.println("Error");     // 标准错误输出
```

| 格式 | 含义 |
| --- | --- |
| `%d` | 整数 |
| `%f` / `%.2f` | 浮点数 / 保留两位小数显示 |
| `%s` | 字符串 |
| `%n` | 换行 |
| `%%` | 百分号 |

输出两个整数并用空格隔开：

```java
System.out.println(a + " " + b);
System.out.printf("%d %d%n", a, b);
```

用双引号空格 `" "`；单引号 `' '` 是字符，参与数字运算时可能被当作字符编码值。
