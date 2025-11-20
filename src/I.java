import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class I {

    record InputData(String keyboard, String[] texts){}

    private static InputData readInput() throws IOException{
        try(var br = new BufferedReader(new InputStreamReader(System.in))){
            var keyboard = br.readLine();
            int n = Integer.parseInt(br.readLine());
            var texts = new String[n];
            for (int i = 0; i < n; i++) {
                var line = br.readLine();
                texts[i] = line.substring(1, line.length() - 1);
            }
            return new InputData(keyboard, texts);
        }
    }
    private static Map<Character, List<Integer>> createMap(String keyboard) {
        var map = new HashMap<Character, List<Integer>>();
        for (int i = 0; i < keyboard.length(); i++) {
            char c = keyboard.charAt(i);
            map.computeIfAbsent(c, k -> new ArrayList<>()).add(i);
        }
        return map;
    }

    private static boolean check(String text, Set<Character> chars){
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if(!chars.contains(c)){
                return false;
            }
        }
        return true;
    }
    private static int solveText(String text, Set<Character> keyboardAsSet, Map<Character, List<Integer>> map, String keyboard){
        if(text.isEmpty()){
            return 0;
        }
        if(!check(text, keyboardAsSet)){
            return -1;
        }


        var prev = new HashMap<Integer, Integer>();
        prev.put(0, 0);
        prev.put(keyboard.length() - 1, 0);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            var curr = new HashMap<Integer, Integer>();
            var positions = map.get(c);
            for(var p : positions){
                int minValue = Integer.MAX_VALUE;
                for(var e : prev.entrySet()){
                    int value = Math.abs(p - e.getKey()) + e.getValue();
                    minValue = Math.min(minValue, value);
                }
                curr.put(p, minValue);
            }
            prev = curr;
        }

        return prev.values().stream().mapToInt(i -> i).min().orElseThrow();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        var inputData = readInput();
        var keyboard = inputData.keyboard;

        var keyboardAsSet = keyboard.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet());

        var keyboardAsMap = createMap(keyboard);


        var results = Arrays.stream(inputData.texts)
                .map(text -> String.valueOf(solveText(text, keyboardAsSet, keyboardAsMap, keyboard)))
                .collect(Collectors.joining(System.lineSeparator()));

        System.out.println(results);

    }
}
