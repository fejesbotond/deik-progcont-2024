import java.util.Locale;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.stream.IntStream;

public class J {

    private static final double EPS = 1e-9;

    private static boolean floatEqual(double d1, double d2) {
        return Math.abs(d1 - d2) <= EPS;
    }

    private record Vec2(double x, double y) {
        Vec2 add(Vec2 other) {
            return new Vec2(this.x + other.x, this.y + other.y);
        }

        Vec2 subtract(Vec2 other) {
            return new Vec2(this.x - other.x, this.y - other.y);
        }

        Vec2 scale(double s) {
            return new Vec2(this.x * s, this.y * s);
        }
    }

    private static double dot(Vec2 a, Vec2 b) {
        return a.x() * b.x() + a.y() * b.y();
    }

    private static double length(Vec2 v) {
        return Math.sqrt(dot(v, v));
    }

    private static Vec2 normalize(Vec2 v) {
        double len = length(v);
        if (floatEqual(len, 0.0)) {
            throw new IllegalArgumentException("Cannot normalize zero-length vector");
        }
        return new Vec2(v.x() / len, v.y() / len);
    }

    /*
        Ci + dc*vc*t = L + dl*vl*t

        After squaring, dl is eliminated (|dl| = 1).

        Let D = Ci - L

        Quadratic in t:
            A = vc^2 - vl^2
            B = 2 * vc * (D · dc)
            C = D · D
    */
    private static double t(Vec2 L, Vec2 Ci, Vec2 dc, double vc, double vl) {
        Vec2 D = Ci.subtract(L);

        double A = vc * vc - vl * vl;
        double B = 2.0 * vc * dot(D, dc);
        double C = dot(D, D);

        double t = -1.0;

        if (floatEqual(A, 0.0)) {
            if (!floatEqual(B, 0.0)) {
                t = -C / B;
            }
        } else {
            t = quadratic(A, B, C);
        }
        return t;
    }

    /*
        |Ci + dc*vc*t - M| = distance

        Let D = Ci - M

        Quadratic in t:
            A = vc^2
            B = 2 * vc * (D · dc)
            C = D · D - distance^2
    */
    private static double tUntilCircle(Vec2 Ci, Vec2 dc, double vc, Vec2 M, double distance) {
        Vec2 D = Ci.subtract(M);

        double A = vc * vc;
        double B = 2.0 * vc * dot(D, dc);
        double C = dot(D, D) - distance * distance;

        return quadratic(A, B, C);
    }

    private static double quadratic(double A, double B, double C) {
        double t = -1.0;
        double discriminant = B * B - 4.0 * A * C;

        if (floatEqual(discriminant, 0.0)) {
            t = -B / (2.0 * A);
        } else if (discriminant > EPS) {
            double sqrtD = Math.sqrt(Math.max(0.0, discriminant));
            double t1 = (-B - sqrtD) / (2.0 * A);
            double t2 = (-B + sqrtD) / (2.0 * A);
            if (t1 > EPS && t2 > EPS)
                t = Math.min(t1, t2);
            else if (t1 > EPS)
                t = t1;
            else if (t2 > EPS)
                t = t2;
            else if(floatEqual(0.0, t1) || floatEqual(0.0, t2))
                t =  0.0;
        }
        return t;
    }

    private static double solve(Vec2[] C, double[] V, Vec2 M, Vec2 L, double Vl, double distance) {
        var D = new Vec2[]{
                normalize(M.subtract(C[0])),
                normalize(M.subtract(C[1])),
                normalize(M.subtract(C[2]))
        };

        boolean isDistanceReached = false;
        double sumDistance = 0.0;

        for (int i = 0; !isDistanceReached; i++) {
            Vec2 Ci = C[i % 3];
            Vec2 Di = D[i % 3];
            double Vi = V[i % 3];

            double t = t(L, Ci, Di, Vi, Vl);

            if (floatEqual(t, 0.0)) {
                continue;
            }
            if (t < -EPS) {
                throw new IllegalStateException("The next Fly direction cannot be determined");
            }

            Vec2 intersectionPoint = Ci.add(Di.scale(Vi * t));
            Vec2 Dl = normalize(intersectionPoint.subtract(L));

            OptionalDouble tMin = IntStream.range(0, 3)
                    .mapToDouble(j -> tUntilCircle(C[j], D[j], V[j], M, distance))
                    .filter(p -> p < t)
                    .min();

            if (tMin.isPresent()) {
                double tPartial = tMin.orElseThrow();
                Vec2 nextL = L.add(Dl.scale(Vl * tPartial));
                sumDistance += length(nextL.subtract(L));
                isDistanceReached = true;
            } else {
                for (int j = 0; j < 3; j++) {
                    C[j] = C[j].add(D[j].scale(t * V[j]));
                }

                Vec2 nextL = L.add(Dl.scale(Vl * t));
                sumDistance += length(nextL.subtract(L));
                L = nextL;
            }
        }
        return sumDistance;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var C1 = new Vec2(scanner.nextInt(), scanner.nextInt());
        double v1 = scanner.nextInt();
        var C2 = new Vec2(scanner.nextInt(), scanner.nextInt());
        double v2 = scanner.nextInt();
        var C3 = new Vec2(scanner.nextInt(), scanner.nextInt());
        double v3 = scanner.nextInt();

        Vec2 M = new Vec2(scanner.nextInt(), scanner.nextInt());
        double distance = scanner.nextInt();
        Vec2 L = new Vec2(scanner.nextInt(), scanner.nextInt());
        double vl = scanner.nextInt();

        var C = new Vec2[]{C1, C2, C3};
        var V = new double[]{v1, v2, v3};

        double result = solve(C, V, M, L, vl, distance);

        System.out.printf(Locale.US, "%.2f%n", result);
    }
}
