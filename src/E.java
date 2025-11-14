import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class E {

    record IntTuple(int first, int second){}

    record InputData(int A, int B, int C, IntTuple[] qs){}

    private static InputData readInput(){
        var scanner = new Scanner(System.in);
        int A = scanner.nextInt();
        int B = scanner.nextInt();
        int C = scanner.nextInt();
        var qs = IntStream.range(0, scanner.nextInt())
                .mapToObj(i -> new IntTuple(scanner.nextInt(), scanner.nextInt()))
                .toArray(IntTuple[]::new);

        return new InputData(A, B, C, qs);
    }

    private static int computeY(long x, int A, int B, int C, Map<Long, Integer> cache){
        var visitedXs = new ArrayList<Long>();
        while(x >= C){
            visitedXs.add(x);
            if( x % 2 != 0){
                x = A*x + B;
            }else{
                x /= C;
            }
        }
        for(int i = 0; i < visitedXs.size(); i++){
            cache.put(visitedXs.get(i), visitedXs.size() - i + 1);
        }
        return visitedXs.size() + 1;
    }

    public static void main(String[] args){
        var data = readInput();
        int A = data.A;
        int B = data.B;
        int C = data.C;
        var qs = data.qs;

        var cache = new HashMap<Long, Integer>();
        var result = new ArrayList<IntTuple>();
        for(var loUp : qs){
            int min = Integer.MAX_VALUE, max = -1;
            for(int x = loUp.first; x <= loUp.second; x++){
                var y = cache.get((long)x);
                if(y == null){
                    y = computeY(x, A, B, C, cache);
                }
                min = Math.min(y, min);
                max = Math.max(y, max);
            }
            result.add(new IntTuple(min, max));
        }

        result.forEach(l -> System.out.println(l.first + " " + l.second));
    }
}
