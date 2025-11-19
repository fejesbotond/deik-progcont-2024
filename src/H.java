import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class H {

    record InputData(int[] x, boolean[] d){}

    private static InputData readInput() throws IOException {
        try(var br = new BufferedReader(new InputStreamReader(System.in))){
            int N = Integer.parseInt(br.readLine());
            var x = new int[N];
            var d = new boolean[N];
            var l1 = new StringTokenizer(br.readLine());
            var l2 = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) {
                x[i] = Integer.parseInt(l1.nextToken());
                d[i] = l2.nextToken().charAt(0) == '+';
            }
            return new InputData(x, d);
        }
    }

    private static int[] getParentTable(int[] x, boolean[] d){
        var parent = new int[x.length];
        Arrays.fill(parent, -1);

        var stack = new ArrayDeque<Integer>();
        for (int i = 0; i < x.length; i++) {
            while (!stack.isEmpty() && x[i] >= x[stack.peek()]) {
                stack.pop();
            }
            if(!stack.isEmpty() && !d[i]){
                parent[i] = stack.peek();
            }
            stack.push(i);
        }
        stack.clear();
        for (int i = x.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && x[i] >= x[stack.peek()]) {
                stack.pop();
            }
            if(!stack.isEmpty() && d[i]){
                parent[i] = stack.peek();
            }
            stack.push(i);
        }
        return parent;
    }

    private static int[] getRootTable(int[] parentTable){
        var rootTable = new int[parentTable.length];
        for(int i = 0; i < parentTable.length; i++){
            int p = i;
            while(parentTable[p] != -1){
                p = parentTable[p];
            }
            rootTable[i] = p;
        }
        return rootTable;
    }

    public static void main(String[] args) throws IOException {
        var inputData = readInput();
        var x = inputData.x;
        var d = inputData.d;

        var rootTable = getRootTable(getParentTable(x, d));

        var componentsWithSum =
                IntStream.range(0, rootTable.length)
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> rootTable[i],
                                i -> (long)x[i],
                                Long::sum
                        ));

        var max = componentsWithSum.values().stream().mapToLong(i -> i).max().orElseThrow();
        var count = componentsWithSum.size();

        System.out.println(count + " " + max);
    }
}
