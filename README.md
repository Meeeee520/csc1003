# csc1003
csc1003课程中遇到的不会的知识点

## Java 复习笔记

根据 ChatGPT 项目「计算机知识点复习」中可检索的讨论与数组总结整理，更新日期：2026-10-09。
采用“概念 → 示例 → 易错点”的方式，合并重复提问并修正容易误解的表述。


| 顺序 | 文件 | 主要内容 |
| --- | --- | --- |
| 1 | [输入输出与 Scanner](notes/01-input-output-scanner.md) | next/nextLine、换行残留、printf |
| 2 | [类型与运算](notes/02-types-and-operators.md) | 整数除法、强转、拼接、浮点数 |
| 3 | [字符串](notes/03-strings.md) | String 方法、拆分、StringBuilder、append |
| 4 | [输入验证与循环](notes/04-validation-and-control-flow.md) | 1–100 整数、try/catch、break/continue |
| 5 | [一维数组](notes/05-arrays-basics.md) | 遍历、统计、复制、随机模拟 |
| 6 | [二维数组](notes/06-two-dimensional-arrays.md) | 行列、嵌套循环、八邻域、扫雷 |
| 7 | [成绩报告练习](notes/07-grade-report.md) | 输入解析、按科目统计、代码修正 |
| 8 | [易错点与自测](notes/08-common-mistakes.md) | 常见错误对照与自测答案 |

## 可运行示例

[examples/GradeReport.java](examples/GradeReport.java) 将输入验证、字符串拆分、二维数组和统计结合起来。
在 examples 目录运行：

```bash
javac GradeReport.java
java GradeReport
```

输入格式和示例见第 7 份笔记；具体作业输出要求以原题为准。
