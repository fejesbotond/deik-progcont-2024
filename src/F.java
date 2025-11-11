import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class F {

    private static long compute(int[] baseNums, int[] exponents, Set<Long> computed, long up, long lo){
        long value = IntStream.range(0, baseNums.length)
                .mapToLong(i -> (long) Math.pow(baseNums[i], exponents[i]))
                .reduce(1L, (a, b) -> a * b);

        if(value > up || !computed.add(value)){
            return 0;
        }

        long count = value >= lo ? 1L : 0L;

        for(int i = 0; i < baseNums.length; i++){
            exponents[i]++;
            count += compute(baseNums, exponents,  computed, up, lo);
            exponents[i]--;
        }

        return count;
    }

    public static void main(String[] args){
        var scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        var baseNums = IntStream.range(0, N)
                .map(i -> scanner.nextInt())
                .toArray();

        int Q = scanner.nextInt();
        var results = new ArrayList<Long>();
        for(int i = 0; i < Q; i++){
            var exponents = new int[N];
            var computedCache = new HashSet<Long>();
            long lo = scanner.nextLong();
            long up = scanner.nextLong();
            results.add(compute(baseNums, exponents, computedCache, up, lo));
        }

        results.forEach(System.out::println);
    }
}
