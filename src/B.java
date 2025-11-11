import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class B {

    private record InputData(int N, int K, List<Coordinate> objects, List<Coordinate> lights){}
    private record Coordinate(int x, int y){}

    private static InputData readInput(){
        try(var scanner = new Scanner(System.in)){
            int N = scanner.nextInt();
            int K = scanner.nextInt();
            var coordinates = IntStream.range(0, N)
                    .mapToObj(i -> new Coordinate(scanner.nextInt(), scanner.nextInt()))
                    .toList();

            var lightMask = new boolean[N];
            for (int i = 0; i < K; i++) {
                lightMask[scanner.nextInt() - 1] = true;
            }
            var objects = new ArrayList<Coordinate>(N - K);
            var lights = new ArrayList<Coordinate>(K);
            for (int i = 0; i < N; i++) {
                var c = coordinates.get(i);
                if(lightMask[i]){
                    lights.add(c);
                }else{
                    objects.add(c);
                }
            }
            return new InputData(N, K,
                    List.copyOf(objects),
                    List.copyOf(lights));
        }
    }
    private static int distanceSquareValue(Coordinate c1, Coordinate c2){
        int x = c1.x - c2.x, y = c1.y - c2.y;
        return x * x + y * y;
    }
    private static double intensityFactor(Coordinate object, List<Coordinate> lights){
        return lights.stream()
                .mapToDouble(light -> 1.0 / distanceSquareValue(object, light))
                .sum();
    }
    public static void main(String[] args){
        var inputData = readInput();
        var objects = inputData.objects();
        var lights = inputData.lights();

        double minIntensityFactor = objects.stream()
                .mapToDouble(o -> intensityFactor(o, lights))
                .min()
                .orElseThrow();

        int S = (int)Math.ceil(1.0 / minIntensityFactor);
        System.out.println(S);
    }
}
