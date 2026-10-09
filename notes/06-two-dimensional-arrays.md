# 06 二维数组、遍历与复制

## 二维数组可以理解为“数组中的数组”

```java
int[][] scores = new int[3][2]; // 3 行、每行 2 列
scores[1][0] = 90;             // 第 2 行、第 1 列
```

在成绩表里约定：`scores[i][j]` = 第 i 个学生的第 j 门科目成绩。

- `scores.length`：行数。
- `scores[i].length`：第 i 行的列数。
- `new int[3][2]` 的每个元素默认是 0。

## 按行遍历与按列统计

```java
// 外层行、内层列：逐个学生查看成绩
for (int i = 0; i < scores.length; i++) {
    for (int j = 0; j < scores[i].length; j++) {
        System.out.println(scores[i][j]);
    }
}

// 外层科目、内层学生：统计每门科目
for (int j = 0; j < 2; j++) {
    int sum = 0;
    for (int i = 0; i < scores.length; i++) {
        sum += scores[i][j];
    }
    double avg = (double) sum / scores.length;
}
```

sum 必须在每门科目开始时清零；平均值在内层循环结束后计算。
按列统计的例子假定每一行都具有相同列数且学生数大于 0。

## 不规则（锯齿）数组

```java
int[][] a = {{1, 2}, {3}, {4, 5, 6}};
for (int i = 0; i < a.length; i++) {
    for (int j = 0; j < a[i].length; j++) {
        System.out.println(a[i][j]);
    }
}
```

不能假定所有行都和 a[0] 一样长。

```java
int[][] b = new int[3][];
b[0] = new int[2];
```

此时 b[1]、b[2] 仍是 null，需要先创建行再访问。

## 二维数组复制与输出

`int[][] b = a;` 共享整个数组。
只用 `Arrays.copyOf(a, a.length)` 复制外层数组，内层各行仍共享。

对非 null 的 int 行逐行复制：

```java
int[][] copy = new int[a.length][];
for (int i = 0; i < a.length; i++) {
    copy[i] = a[i] == null ? null : a[i].clone();
}
System.out.println(java.util.Arrays.deepToString(copy));
```

二维数组内容比较可用 `Arrays.deepEquals`；普通 Arrays.equals 对内层数组比较的是引用。

## 扫雷：统计一个格子的八个邻居

若约定 `board[i][j] == 1` 表示地雷，非地雷格需要统计周围八个方向的地雷数。可以让行、列偏移量 `di`、`dj` 分别从 -1 到 1。

```java
static int countAdjacentMines(int[][] board, int i, int j) {
    int count = 0;
    for (int di = -1; di <= 1; di++) {
        for (int dj = -1; dj <= 1; dj++) {
            if (di == 0 && dj == 0) {
                continue; // 跳过格子自己
            }

            int ni = i + di;
            int nj = j + dj;
            if (ni >= 0 && ni < board.length
                    && nj >= 0 && nj < board[ni].length
                    && board[ni][nj] == 1) {
                count++;
            }
        }
    }
    return count;
}
```

用 `StringBuilder` 生成整个结果网格：

```java
StringBuilder output = new StringBuilder();
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        if (board[i][j] == 1) {
            output.append('*');
        } else {
            output.append(countAdjacentMines(board, i, j));
        }
    }
    output.append('\n');
}
System.out.print(output);
```

### 易错点

- 必须先检查 `ni`、`nj` 是否越界，再访问 `board[ni][nj]`。
- `(di, dj) == (0, 0)` 代表当前格子，不应算作邻居。
- `i`、`j` 是当前格坐标，`ni`、`nj` 才是正在检查的邻居坐标。
- 每处理一个新格子，都要重新令 `count = 0`。
- `if` 条件必须写括号：`if (board[i][j] == 1) {`。

## 补充例题：矩阵乘法的三层循环

A 为 r×k，B 为 k×c 时，结果 C 为 r×c。

```java
int[][] result = new int[r][c];
for (int i = 0; i < r; i++) {
    for (int j = 0; j < c; j++) {
        for (int t = 0; t < k; t++) {
            result[i][j] += A[i][t] * B[t][j];
        }
    }
}
```

最内层把 A 的第 i 行与 B 的第 j 列对应元素相乘再累加。此例假定矩阵尺寸匹配且元素计算不溢出。
