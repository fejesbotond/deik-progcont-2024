import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class D {

    private record InputData(int[] grades, int T, int[][] t){}

    private static InputData readInput(){
        var scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        var grades = IntStream.range(0, N).map(i -> scanner.nextInt()).toArray();
        int T = scanner.nextInt();
        var t = IntStream.range(0, N)
                .mapToObj(i -> IntStream.range(0, 4)
                        .map(j -> scanner.nextInt())
                        .toArray())
                .toArray(int[][]::new);

        return new InputData(grades, T, t);
    }

    private static int fix1s(int[] grades, int T, int[][] t){
        for (int i = 0; i < grades.length; i++) {
            if(grades[i] == 1){
                if(T < t[i][0]){
                    return -1;
                }
                T -= t[i][0];
                grades[i] = 2;
            }
        }
        return T;
    }

    private static void fix(int[] grades, int T, int[][] t){
        while(true){
            int min = Integer.MAX_VALUE;
            int indexAtMin = -1;
            for (int i = 0; i < grades.length; i++) {
                if(grades[i] != 5){
                    if(t[i][grades[i] - 1] < min){
                        min = t[i][grades[i] - 1];
                        indexAtMin = i;
                    }
                }
            }
            if(indexAtMin == -1 || T < min){
                break;
            }
            T -= min;
            grades[indexAtMin]++;
        }
    }

    public static void main(String[] args){
        var inputData = readInput();
        var t = inputData.t;
        var T = inputData.T;
        var grades = inputData.grades;

        T = fix1s(grades, T, t);

        if(T == -1){
            System.out.println(":-(");
        } else {
            fix(grades, T, t);
            double avg = Arrays.stream(grades).average().orElseThrow();
            System.out.printf("%.2f%n", avg);
        }
    }
}
