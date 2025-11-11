import java.util.Scanner;

public class L {

    private record InputData(int N, int M, int H, int W) {}

    private static InputData readInput() {
        try (var sc = new Scanner(System.in)) {
            return new InputData(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        }
    }

    private static void printChars(char c, int count) {
        for (int i = 0; i < count; i++) System.out.print(c);
    }

    public static void main(String[] args) {
        var in = readInput();
        int N = in.N(), M = in.M(), H = in.H(), W = in.W();

        printChars(' ', M);
        System.out.println('x');

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                printChars(' ', M - j - 1);
                System.out.print('x');
                printChars('.', j * 2 + 1);
                System.out.println('x');
            }
        }

        for (int i = 0; i < H; i++) {
            printChars(' ', (2 * M + 1 - W) / 2);
            printChars('x', W);
            System.out.println();
        }
    }
}

