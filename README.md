# csc1003
csc1003课程中遇到的不会的知识点

## Java 复习笔记

根据 ChatGPT 项目「计算机知识点复习」中可检索的讨论与数组总结整理，更新日期：2026-10-21。
采用“概念 → 示例 → 易错点”的方式，合并重复提问并修正容易误解的表述。


| 顺序 | 文件 | 主要内容 |
| --- | --- | --- |
| 1 | [输入输出、Scanner 与 Reader](notes/01-input-output-scanner.md) | 变量初始化、标准输入、token/整行、FileReader/StringReader、printf |
| 2 | [类型与运算](notes/02-types-and-operators.md) | 基本类型位宽、整数除法、强转、短路求值、溢出、自增、字符串拼接 |
| 3 | [字符串](notes/03-strings.md) | String 方法、trim/strip、不可变性、StringBuilder |
| 4 | [输入验证与循环](notes/04-validation-and-control-flow.md) | 1–100 整数、if/else if、异常层级、try/catch/throws、循环控制 |
| 5 | [一维数组](notes/05-arrays-basics.md) | for-each、相邻元素统计、复制、随机模拟 |
| 6 | [二维数组](notes/06-two-dimensional-arrays.md) | 行列、浅拷贝、矩阵乘法、八邻域、扫雷 |
| 7 | [成绩报告练习](notes/07-grade-report.md) | 输入解析、按科目统计、代码修正 |
| 8 | [易错点与自测](notes/08-common-mistakes.md) | 常见错误对照与自测答案 |
| 9 | [包与 import](notes/09-packages-and-imports.md) | 包名、单类导入、通配符、全限定名 |
| 10 | [方法参数与值传递](notes/10-methods-and-parameters.md) | 实参与形参、return/void、static、值传递、作用域 |
| 11 | [期中综合：程序追踪、命令行与 LFSR](notes/11-midterm-program-tracing.md) | 编译运行、命令行参数、循环追踪、二进制、LFSR、JVM、重定向 |

## 可运行示例

[examples/GradeReport.java](examples/GradeReport.java) 将输入验证、字符串拆分、二维数组和统计结合起来。
在 examples 目录运行：

```bash
javac GradeReport.java
java GradeReport
```

输入格式和示例见第 7 份笔记；具体作业输出要求以原题为准。
