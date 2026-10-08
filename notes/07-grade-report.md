# 07 成绩报告：把知识点串起来

## 本次练习的输入结构

1. 学生数 n：1–100 的整数。
2. 科目名称：1–10 个，用空白分隔。
3. 接下来 n 行：姓名 + m 个整数成绩。

示例：

```text
3
Math English
Alice 90 80
Bob 70 100
Carol 80 90
```

这里 m=2。成绩行拆分后，parts[0] 是姓名，parts[j+1] 才是第 j 门课的成绩。
此格式假定姓名不含空格。

## 程序的核心顺序

读取并验证人数 → 处理行尾 → 读取科目 → 创建二维数组 → 读取每位学生 → 按科目统计 → printf 输出。

```java
int[][] scores = new int[n][m];
for (int i = 0; i < n; i++) {
    String[] parts = sc.nextLine().trim().split("\\s+");
    for (int j = 0; j < m; j++) {
        scores[i][j] = Integer.parseInt(parts[j + 1]);
    }
}
```

这段核心逻辑假定行格式正确。完整教学示例另外检查字段数和非整数，见 [GradeReport.java](../examples/GradeReport.java)。

## 每门课的统计

```java
for (int j = 0; j < m; j++) {
    int sum = 0;
    int high = scores[0][j];
    int low = scores[0][j];
    for (int i = 0; i < n; i++) {
        int value = scores[i][j];
        sum += value;
        high = Math.max(high, value);
        low = Math.min(low, value);
    }
    double avg = (double) sum / n;
    System.out.printf("%s: avg=%.2f high=%d low=%d%n",
            subjects[j], avg, high, low);
}
```

重点：
- 外层 j 是科目，内层 i 是学生；不能把维度写反。
- sum/high/low 在每门课开始时初始化。
- avg 和 printf 放在内层循环之后，才是完整科目的统计。
- `(double) sum / n` 避免整数除法。

## 原代码修改重点

- 首行 `mport` 改成 `import`。
- `nextInt()` 后读取下一行前，先处理本行剩余内容。
- 科目行先 trim，拒绝空行，再 split。
- 成绩行先 trim，再拆分；先检查 m+1 个字段。
- Scanner 非整数异常是 InputMismatchException；parseInt 非整数异常是 NumberFormatException。

完整示例的输出格式是教学示范；若作业规定标题、空格、精度或错误恢复方式，应以题目为准。现有记录未给出成绩范围，因此示例只验证 int 格式，未自行添加成绩必须在 0–100 的规则。
