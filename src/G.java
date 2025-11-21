import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class G {

    private static int[][] readInput() throws IOException {
        try(var br = new BufferedReader(new InputStreamReader(System.in))){
            int n = Integer.parseInt(br.readLine());
            var data = new int[n][6];
            for (int i = 0; i < n; i++) {
                var tokens = br.readLine().split(" ");
                for(int j = 0; j < 6; j++){
                    data[i][j] = Integer.parseInt(tokens[j]);
                }
            }
            return data;
        }
    }
    //O(n^3)
    public static void main(String[] args) throws IOException{
        var data = readInput();
        int count = 0;
        for (int i = 0; i < data.length - 2; i++) {
            for (int j = i + 1; j < data.length - 1; j++) {
                for (int k = j + 1; k < data.length; k++) {
                    if(nonTransitive(data[i], data[j], data[k])){
                        count++;
                    }
                }
            }
        }
        System.out.println(count);
    }

    private static boolean nonTransitive(int[] a, int[] b, int[] c){
        return isBetter(a, b) && isBetter(b, c) && isBetter(c, a) ||
               isBetter(b, a) && isBetter(a, c) && isBetter(c, b);
    }

    private static boolean isBetter(int[] worse, int[] better){
        int count = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if(better[i] > worse[j]){
                    count++;
                }
            }
        }
        return count > 18;
    }
}
