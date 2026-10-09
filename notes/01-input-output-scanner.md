# 01 输入输出与 Scanner

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
