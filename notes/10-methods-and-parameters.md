# 10 方法参数与值传递

## 概念：Java 永远使用值传递

调用方法时，Java 会先计算每个实参的值，再把这些值复制给方法的形参。

- **实参**：调用方法时写进去的内容，例如 `modify(a, nums)` 中的 `a`、`nums`。
- **形参**：方法声明中接收值的局部变量，例如 `modify(int x, int[] arr)` 中的 `x`、`arr`。
- **值传递**：形参拿到的是实参值的一份副本，不是调用者变量本身的另一个名字。

## 基本类型：复制具体数值

```java
public class PassByValue {
    public static void main(String[] args) {
        int a = 5;
        int[] nums = {5};

        modify(a, nums);

        System.out.println(a + " " + nums[0]); // 5 10
    }

    static void modify(int x, int[] arr) {
        x = 10;
        arr[0] = 10;
    }
}
```

执行 `modify(a, nums)` 时，基本类型部分可以这样追踪：

| 位置 | 变量 | 保存的值 |
| --- | --- | --- |
| `main` | `a` | 5 |
| `modify` 刚开始 | `x` | 从 `a` 复制来的 5 |
| 执行 `x = 10` 后 | `x` | 10 |
| 返回 `main` 后 | `a` | 仍然是 5 |

`x = 10` 的意思是“把 10 赋给当前方法中的局部变量 `x`”。调用时的逗号只用于分隔多个实参，不表示把逗号左边的变量改成某个值。

## 为什么名字相同也不是同一个变量

变量属于各自的作用域。调用者中的变量和被调用方法的形参位于不同的方法中，因此是两个独立变量：

```java
static void change(int a) {
    a = 10; // 这里的 a 是 change 的形参
}

public static void main(String[] args) {
    int a = 5; // 这里的 a 是 main 的局部变量
    change(a);
    System.out.println(a); // 5
}
```

两个变量恰好都叫 `a`，不会让它们变成同一个变量。判断时看“变量声明在哪个方法或代码块里”，不要只看名字。

这是 Java 语言规定的参数传递规则：调用 `change(a)` 时，`main` 中 `a` 保存的 5 被复制给 `change` 的形参 `a`。

## 数组：复制的是引用值

数组变量保存的不是全部数组元素，而是指向数组对象的引用。传入数组时，复制的是这个引用值：

```java
static void modify(int x, int[] arr) {
    x = 10;      // 只改基本类型副本
    arr[0] = 10; // 通过引用找到并修改共享数组
}
```

调用者的 `nums` 和形参 `arr` 是两个变量，但它们保存的引用指向同一个数组。因此 `arr[0] = 10` 会让 `nums[0]` 也变成 10。

这仍然是值传递，不是“引用传递”：复制过去的值恰好是一个引用。

## 修改数组与重新绑定形参的区别

```java
static void replace(int[] arr) {
    arr[0] = 10;            // 修改共享数组，调用者能看到
    arr = new int[] {99};   // 只让局部变量 arr 指向新数组
}
```

- `arr[0] = 10`：沿着复制来的引用修改原数组。
- `arr = new int[] {99}`：只改变形参 `arr` 保存的引用，不会让调用者的数组变量改为指向新数组。

## 想修改基本类型变量怎么办？

可以让方法返回新值，再由调用者主动赋值：

```java
static int change(int x) {
    return 10;
}

public static void main(String[] args) {
    int a = 5;
    a = change(a);
    System.out.println(a); // 10
}
```

真正改变 `main` 中 `a` 的是 `a = change(a)` 这次赋值。

## 返回值、void 与职责分离

方法声明中的返回类型决定调用表达式能不能产生一个值：

- `double` 方法必须返回一个可转换为 `double` 的值，调用者可以接收或继续使用它。
- `void` 表示方法不返回结果，因此不能把它赋给变量。
- `System.out.println(...)` 只是把内容显示到控制台，不会把打印的内容变成方法返回值。

例如，只把“计算平均值”提取为方法，并把打印保留在 `main`：

```java
public class AverageDemo {
    static double calculateAverage(int[] scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return (double) total / scores.length;
    }

    public static void main(String[] args) {
        int[] scores = {80, 90, 100};
        double average = calculateAverage(scores);
        System.out.println("Average: " + average);
    }
}
```

这里的流程是：

1. `main` 把数组引用值传给 `calculateAverage`。
2. 方法完成求和与平均值计算，并用 `return` 把结果交回调用处。
3. `main` 保存返回值并负责打印。

如果方法声明为 `void`，就不能写 `double average = calculateAverage(scores);`，因为右侧没有值可以赋给 `average`。如果方法内部直接打印，虽然可能看到相同文字，却没有满足“打印保留在 `main`”的设计要求，也降低了方法的复用性：其他代码无法直接取得平均值做比较或后续计算。

### 易错点

- “打印结果”和“返回结果”是两件事：`println` 负责显示，`return` 负责把值传给调用者。
- 返回类型必须与实际用途匹配；平均值可能有小数，应返回 `double`，而不是 `int`。
- 抽取方法时要遵守题目规定的职责边界；若只要求抽取计算，就不要顺便把输出也移入方法。
- 方法自己的局部变量（如 `total`）应留在方法内部，调用者只需要关心参数和返回值。

## `main` 与 `static` 方法

程序入口是：

```java
public static void main(String[] args)
```

`main` 是静态方法。从 `main` 直接调用同一个类中的辅助方法时，该方法通常也要声明为 `static`：

```java
public class StaticDemo {
    static int square(int x) {
        return x * x;
    }

    public static void main(String[] args) {
        System.out.println(square(5)); // 25
    }
}
```

如果辅助方法不是 `static`，它属于对象，必须先创建对象，再通过对象调用。

易错点：`static` 不表示“任何地方都能访问”。能否访问还受访问修饰符和作用域影响；这里只说明静态方法可以不依赖对象实例而调用。

## 易错点

- Java 的方法参数永远是值传递。
- 基本类型参数复制具体数值，给形参重新赋值不会改变实参变量。
- 数组和对象参数复制引用值；形参与实参变量不同，但可能指向同一个对象。
- 通过引用修改对象内部内容，调用者通常能看到。
- 给引用形参重新赋值，只改变形参自己指向哪里。
- 形参与调用者变量同名，也仍然是不同作用域中的不同变量。
- 方法调用中的逗号只分隔实参，不表示赋值关系。
