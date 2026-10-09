# 03 String 方法与文本拆分

字符串下标从 0 开始，String 不可变：调用转换方法通常会返回新字符串。

```java
String s = "Hello";
s.toUpperCase();     // 没有把结果保存，s 仍为 Hello
s = s.toUpperCase(); // s 现在引用 HELLO
```

## 课上讨论的方法

以下以 `String s = "Hello";` 为例：

| 方法 | 作用 | 例子及结果 |
| --- | --- | --- |
| `length()` | 字符串长度 | `s.length()` → 5 |
| `charAt(i)` | 第 i 个 char | `s.charAt(1)` → `'e'` |
| `substring(a, b)` | 截取 [a, b) | `s.substring(1, 4)` → `"ell"` |
| `substring(a)` | 从 a 到结尾 | `s.substring(2)` → `"llo"` |
| `toUpperCase()` | 转大写 | `"HELLO"` |
| `equals(other)` | 比较内容 | `s.equals("Hello")` → true |
| `isEmpty()` | 长度是否为 0 | `"".isEmpty()` → true |
| `indexOf(ch)` | 第一次出现的位置 | `s.indexOf('l')` → 2；找不到返回 -1 |

`charAt` 合法下标是 0 到 `length() - 1`。
`substring` 左闭右开，末尾位置可以等于字符串长度。
比较字符串内容用 `equals`，`==` 比较引用是否相同。
`" ".isEmpty()` 为 false，因为它包含一个空格。

## 成绩报告中用到的 trim 与 split

```java
String line = sc.nextLine().trim();
if (line.isEmpty()) {
    System.out.println("Invalid input");
} else {
    String[] parts = line.split("\\s+");
}
```

- `trim()` 去掉首尾普通空白；中间空格不会被删除。
- `split("\\s+")` 按连续的空白字符拆分，得到 String 数组。
- Java 字符串里的 `\\` 表示一个反斜杠，正则 `\s` 表示空白，`+` 表示一次或多次。
- 先拒绝空行：空字符串拆分出的数组并不代表有一个有效科目。
- 先 trim 可以避免开头空格造成第一个片段为空字符串。

```java
String[] parts = "Alice 90 85".split("\\s+");
int math = Integer.parseInt(parts[1]); // 把 "90" 转成 90
```

`Integer.parseInt` 遇到非整数文本或超出 int 范围的文本会抛出 `NumberFormatException`，与 Scanner 的 `InputMismatchException` 不同。

## 用 charAt 读取数字字符

字符数字可以通过减去字符 `'0'` 转成对应的整数：

```java
char ch = '7';
int value = ch - '0'; // 7
```

这是因为字符在计算时会使用字符编码值，`'7'` 与 `'0'` 的编码差正好是 7。读取一整行 0/1 网格时，应先验证字符，再转换：

```java
boolean valid = line.length() == cols;
for (int j = 0; valid && j < cols; j++) {
    char ch = line.charAt(j);
    if (ch != '0' && ch != '1') {
        valid = false;
    }
}

if (valid) {
    for (int j = 0; j < cols; j++) {
        board[row][j] = line.charAt(j) - '0';
    }
}
```

### 易错点

- `line.length() == cols` 只检查长度，不保证内容一定是 0 或 1。
- 对字母等非数字字符直接做 `ch - '0'` 也会得到一个整数，但这个整数没有题目需要的含义。
- `'0'` 是字符，数值是 0；写成 `ch - 0` 不会完成数字字符转换。

## StringBuilder：循环中逐步拼接输出

`StringBuilder` 是一个可修改的字符序列。需要在循环里不断追加字符时，可以先把内容放进去，最后一次输出。

```java
StringBuilder output = new StringBuilder();
output.append('*');
output.append(3);
output.append('\n');
System.out.print(output);
```

- `new StringBuilder()` 创建一个空的“文字容器”。
- `output.append('*')` 把字符 `*` 加到末尾；它不会立刻打印。
- `append(3)` 会追加字符形式的 `3`，不是进行加法。
- `append('\n')` 追加换行符。
- 最后 `print(output)` 时，Java 会输出其中积累的全部内容。

### 易错点

- `'*'` 是一个 `char`，`"*"` 是一个 `String`；两者都能传给 `append`，但类型不同。
- `StringBuilder` 没有自动换行，需要自己追加 `\n`，或最后用 `println`。
- 如果每一行都使用同一个 builder，记得在合适位置追加换行；如果只想清空，可用 `output.setLength(0)`。
