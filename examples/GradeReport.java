import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/** 教学示例：姓名不含空格，成绩接受 int 格式；输出格式不是原题的承诺。 */
public class GradeReport {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n;

        while (true) {
            if (!sc.hasNext()) return;
            try {
                n = sc.nextInt();
                if (n >= 1 && n <= 100) break;
            } catch (InputMismatchException e) {
                sc.nextLine(); // 按行恢复：丢弃错误行剩余内容
                System.out.println("Invalid input");
                continue;
            }
            if (sc.hasNextLine()) sc.nextLine();
            System.out.println("Invalid input");
        }
        if (!sc.hasNextLine()) return;
        sc.nextLine(); // 人数与科目分行输入

        String[] subjects;
        while (true) {
            if (!sc.hasNextLine()) return;
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                subjects = line.split("\\s+");
                if (subjects.length <= 10) break;
            }
            System.out.println("Invalid input");
        }

        int m = subjects.length;
        int[][] scores = new int[n][m];
        for (int i = 0; i < n;) {
            if (!sc.hasNextLine()) return;
            String line = sc.nextLine().trim();
            String[] parts = line.split("\\s+");
            if (line.isEmpty() || parts.length != m + 1) {
                System.out.println("Invalid input");
                continue;
            }
            try {
                int[] row = new int[m];
                for (int j = 0; j < m; j++) {
                    row[j] = Integer.parseInt(parts[j + 1]);
                }
                scores[i] = row; // 整行合法后再保存
                i++;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }

        for (int j = 0; j < m; j++) {
            long sum = 0; // 减少多个 int 成绩相加时溢出的风险
            int high = scores[0][j];
            int low = scores[0][j];
            for (int i = 0; i < n; i++) {
                sum += scores[i][j];
                high = Math.max(high, scores[i][j]);
                low = Math.min(low, scores[i][j]);
            }
            double avg = (double) sum / n;
            System.out.printf(Locale.ROOT, "%s: avg=%.2f high=%d low=%d%n",
                    subjects[j], avg, high, low);
        }
    }
}
