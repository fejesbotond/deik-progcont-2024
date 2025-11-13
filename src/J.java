import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class J {
    private record Vec2(int x, int y){}

    private static double distance(Vec2 a, Vec2 b){
        int x = a.x - b.x, y = a.y - b.y;
        return Math.sqrt(x * x + y * y);
    }

    private record Obj(Vec2 position, int velocity){}

    private record InputData(Obj[] carts, Obj fly, Vec2 marketPosition, int distance){}

    private static InputData readInput() {
        var sc = new Scanner(System.in);

        var carts = new Obj[3];
        for (int i = 0; i < 3; i++) {
            var pos = new Vec2(sc.nextInt(), sc.nextInt());
            int v = sc.nextInt();
            carts[i] = new Obj(pos, v);
        }

        var marketPosition = new Vec2(sc.nextInt(), sc.nextInt());
        int distance = sc.nextInt();

        var flyPos = new Vec2(sc.nextInt(), sc.nextInt());
        int flyVelocity = sc.nextInt();
        var fly = new Obj(flyPos, flyVelocity);

        return new InputData(carts, fly, marketPosition, distance);
    }

    public static void main(String[] args){
        var inputData = readInput();
        var carts = inputData.carts();
        var fly = inputData.fly();
        var marketPos = inputData.marketPosition();
        int distance = inputData.distance;

        double t = Arrays.stream(carts)
                .mapToDouble(c -> (- distance + distance(c.position(), marketPos)) / c.velocity())
                .min()
                .orElseThrow();

        var totalFlyDistance = t * fly.velocity();

        System.out.printf(Locale.US, "%.2f%n", totalFlyDistance);

    }
}
