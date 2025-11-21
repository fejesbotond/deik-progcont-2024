import java.util.Locale;
import java.util.Scanner;
import java.util.stream.IntStream;

public class K {

    record InputData(double[] s1, double[] s2){}

    private static InputData readInput(){
        var scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        int n = scanner.nextInt();
        var s1 = IntStream.range(0, n).mapToDouble(i -> scanner.nextDouble()).toArray();
        var s2 = IntStream.range(0, n).mapToDouble(i -> scanner.nextDouble()).toArray();
        return new InputData(s1, s2);
    }

    private static void compute(double[] s1, double[] s2, boolean[] v, int level, double[] r){
        if(level == v.length){
            if(check(v)){
                r[0] += P(v, s1);
                r[1] += P(v, s2);
            }
            return;
        }
        v[level] = true;
        compute(s1, s2, v, level + 1, r);
        v[level] = false;
        compute(s1, s2, v, level + 1, r);
    }


    private static double P(boolean[] variation, double[] s){
        double P = 1.0;
        for (int i = 0; i < variation.length ; i++) {
            double p = variation[i] ? s[i] : 1.0 - s[i];
            P *= p;
        }
        return P;
    }

    private static boolean check(boolean[] variation){
        for (int i = 0; i < variation.length - 2; i++) {
            if(variation[i] && variation[i+1] && variation[i+2]){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args){
        var inputData = readInput();
        var s1 = inputData.s1;
        var s2 = inputData.s2;

        var result = new double[2];
        var variation = new boolean[s1.length];
        compute(s1, s2, variation, 0, result);

        int flag = 2;
        if(Math.abs(result[0] - result[1]) < 1e-12){
            flag = 0;
        }
        else if(result[0] < result[1]){
            flag = 1;
        }
        System.out.println(flag);
    }
}
