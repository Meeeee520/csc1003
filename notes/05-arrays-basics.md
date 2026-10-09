# 05 一维数组与常用算法

## 创建、默认值与索引

```java
int[] a = new int[5];       // 5 个 int，初始都是 0
int[] b = {10, 20, 30};
String[] names = new String[3]; // 初始都是 null
boolean[] seen = new boolean[12]; // 初始都是 false
```

数组保存同一声明类型的元素，创建后长度固定。
下标从 0 到 `a.length - 1`。数组长度是 `a.length`，不是 `a.length()`。
访问 `a[a.length]` 会越界；引用为 null 时访问数组会出错。

## 为什么数组的 length 不加括号

括号 `()` 表示“调用方法”。Java 数组的 `length` 不是方法，而是数组对象自带的只读字段，所以直接读取：

```java
int[] numbers = {10, 20, 30};
int n = numbers.length; // 读取字段，结果是 3
```

可以把两种写法理解为：

- 字段：对象保存的数据，用 `对象.字段`。
- 方法：对象提供的操作，用 `对象.方法()`。

Java 对不同类型采用了不同接口：

| 类型 | 获取长度或元素个数 | 原因 |
| --- | --- | --- |
| 数组 | `array.length` | `length` 是数组的只读字段 |
| `String` | `text.length()` | `length()` 是 String 类的方法 |
| `ArrayList` | `list.size()` | `size()` 是集合的方法 |

```java
String text = "Java";
int[] array = {1, 2, 3};
java.util.ArrayList<Integer> list = new java.util.ArrayList<>();

System.out.println(text.length()); // 4
System.out.println(array.length);  // 3
System.out.println(list.size());   // 0
```

### 易错点

- 写 `array.length()` 会报错，因为数组没有 `length()` 方法。
- 写 `text.length` 也会报错，因为 String 没有可供这样读取的 `length` 字段。
- 数组创建后长度固定，因此不能给 `array.length` 重新赋值。

## 普通遍历与 for-each

```java
for (int i = 0; i < a.length; i++) {
    a[i] = i * 2;
}

for (int value : a) {
    System.out.println(value);
}
```

冒号可读作“依次取出 a 中的元素给 value”。

```java
for (int value : a) {
    value = 100; // 改的是局部变量，不会把 a 的元素改成 100
}
```

需要改元素或使用下标时用普通 for。对于对象数组，for-each 得到的是引用副本，可以通过它修改对象状态，但重新赋值循环变量不会替换数组元素。

## 求和、平均、最大与最小

```java
int[] scores = {80, 90, 70};
int sum = 0;
int high = scores[0];
int low = scores[0];

for (int score : scores) {
    sum += score;
    if (score > high) high = score;
    if (score < low) low = score;
}
double average = (double) sum / scores.length;
```

以上假定数组非空。最大最小值从第一个元素初始化，适用于包含负数的数据。
不要用 0 当通用最小值。空数组不能访问 scores[0]，也不能按普通方式求平均。

## 筛选、ALL 与 ANY

```java
int passCount = 0;
boolean allPass = true;
boolean anyPass = false;
for (int score : scores) {
    if (score >= 60) {
        passCount++;
        anyPass = true;
    } else {
        allPass = false;
    }
}
```

ALL 从 true 开始，遇到反例变 false；ANY 从 false 开始，遇到符合条件的元素变 true。

## 赋值不等于复制

```java
int[] x = {1, 2, 3};
int[] y = x;
y[0] = 99; // x[0] 也变成 99：同一个数组
```

独立复制可用逐元素复制或 `Arrays.copyOf`。

## Arrays 工具类

```java
import java.util.Arrays;

int[] original = {3, 1, 2};
int[] copy = Arrays.copyOf(original, original.length);
System.out.println(Arrays.toString(original)); // [3, 1, 2]
System.out.println(Arrays.equals(original, copy)); // true
Arrays.sort(copy); // 直接改变 copy，变成 [1, 2, 3]
```

copyOf 的新长度更大时，多出的 int 元素补 0；更小时截短。
直接 println 数组通常不会显示逐个元素。

## 随机数、骰子与盲盒

`Math.random()` 范围是 [0, 1)。

```java
int die = (int) (Math.random() * 6) + 1; // 1–6
int[] counts = new int[6];
for (int i = 0; i < 1000; i++) {
    int index = (int) (Math.random() * 6); // 0–5
    counts[index]++;
}
```

盲盒去重统计：

```java
boolean[] hasSeen = new boolean[12];
int unique = 0;
int draws = 0;
while (unique < hasSeen.length) {
    int index = (int) (Math.random() * hasSeen.length);
    draws++;
    if (!hasSeen[index]) {
        hasSeen[index] = true;
        unique++;
    }
}
System.out.println(draws);
```

draws 是抽取总次数，unique 是不同款式数；抽到重复款不能增加 unique。
