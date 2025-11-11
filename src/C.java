import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class C {

    public static void main(String[] args) {
        var sc = new Scanner(System.in);
        int Q = sc.nextInt();
        var resultStr = IntStream.range(0, Q)
                .mapToObj(i -> String.valueOf(solve(sc.nextLong(), sc.nextLong())))
                .collect(Collectors.joining(System.lineSeparator()));

        System.out.println(resultStr);
    }

    private static long sequenceSum(long i1, long iN){
        return (i1 + iN) * (iN - i1 + 1) / 2;
    }

    private static long valueAtIdx(long idx, long upperBlockNumber){
        long idxOfUpperBlockNumber = sequenceSum(1, upperBlockNumber);
        long subSequenceSize = idxOfUpperBlockNumber - idx + 1;
        return upperBlockNumber - subSequenceSize + 1;
    }

    private static long solve(long lo, long up){
        long upperBlockNumberLo = findBlockNumber(lo);
        long upperBlockNumberUp = findBlockNumber(up);

        if(upperBlockNumberUp == upperBlockNumberLo){
            return sequenceSum(
                    valueAtIdx(lo, upperBlockNumberLo),
                    valueAtIdx(up, upperBlockNumberLo)
            );
        }

        long loPartial = sequenceSum(valueAtIdx(lo, upperBlockNumberLo), upperBlockNumberLo);

        long upPartial = sequenceSum(1, valueAtIdx(up, upperBlockNumberUp));

        long between = sumTo(upperBlockNumberUp - 1) - sumTo(upperBlockNumberLo);

        return loPartial + between + upPartial;
    }

    private static long findBlockNumber(long k){
        double n = -0.5 + Math.sqrt(0.25 + 2.0*k);
        return (long)Math.ceil(n);
    }

    /*
        Σ (i*(i+1)/2) = 1/2 * (Σ i² + Σ i)
        Σ i = n(n+1)/2)
        Magic: Σ i² = (1/3)·n³ + (1/2)·n² + (1/6)·n

        ==> 1/2 * ((1/3)·n³ + (1/2)·n² + (1/6)·n + (n(n+1)/2)) = n*(n+1)*(n+2)/6
     */
    static long sumTo(long n) {
        return n*(n+1)*(n+2)/6;
    }

}
