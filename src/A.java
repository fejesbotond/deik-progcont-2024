import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args){
        var scanner = new Scanner(System.in);
        int G = scanner.nextInt();

        var str = solve(G).stream()
                .filter(n -> n >= 1 && n <= 99)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        System.out.println(str);
    }

    private static Set<Integer> solve(int G){
        var res = new HashSet<Integer>();
        for(int A = 0; A <= G; A++){
            for(int B = 0; B <= G; B++){
                for(int a = 0; a <= A; a++){
                    for(int H = a; H <= B + a; H++){
                        long numerator = nCk(A, a) * nCk(B, H - a);
                        long denominator = nCk(A + B, H);
                        if((numerator * 100) % denominator == 0){
                            int percent = (int)(numerator * 100 / denominator);
                            res.add(percent);
                        }
                    }
                }
            }
        }
        return res;
    }

    private static long nCk(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;

        if (k > n - k) {
            k = n - k;
        }

        long result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - (k - i));
            result = result / i;
        }

        return result;
    }
}
