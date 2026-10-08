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

## break、continue 与循环层级

- `break`：结束最近的一层循环。
- `continue`：跳过本轮剩余代码，进入最近一层循环的下一轮。
- for 循环中 continue 后仍执行更新表达式，再判断条件。
- 嵌套循环里的 break 不会自动结束所有循环。
- 有效输入计数只在成功时增加，比先增加再 k-- 更容易理解。
