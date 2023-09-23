import java.util.*;

public class Main {

    public static void main(String[] args) {

        // ------------------------ Useful Arrays Syntax ----------------------------------

        int[] arrCopy = Arrays.copyOf(arr, arr.length); // Create a copy of an array
        Arrays.sort(arr); // Sort an array by TimSort

        // ------------------------ Useful List Syntax ------------------------------------

        List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));

        int size = list.size(); // Get the size of a list
        boolean isEmpty = list.isEmpty(); // Check if a list is empty
        boolean contains = list.contains(3); // Check if a list contains an element

        Collections.sort(list); // sort a list by Timsort

        // ------------------------ Useful Set Syntax -------------------------------------

        Set<Integer> set = new HashSet<>(Arrays.asList(5, 2, 8, 1, 9));

        // ------------------------ Useful Map Syntax -------------------------------------

        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 95);
        map.put("Bob", 85);
        map.put("Carol", 90);

        int value = map.get("Alice"); // Get the value associated with a key
        boolean containsKey = map.containsKey("Bob"); // Check if a map contains a key

        // Iterating over map entries
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int val = entry.getValue();
            System.out.println(key + ": " + val);
        }

        // ------------------------ Useful Math Syntax ------------------------------------

        double num = 3.7;

        Math.floor(num);
        Math.round(num);
        Math.ceil(num);
        Math.pow(2, 3);  // 8.0
        Math.sqrt(25);   // 5.0
        Math.abs(-7);    // 7

        // ------------------------ Useful String Syntax ---------------------------------

        String str = "Hello, World!";
        int length = str.length();
        char firstChar = str.charAt(0);
        char lastChar = str.charAt(str.length() - 1);
        int intValue = Integer.parseInt("123");
        double doubleValue = Double.parseDouble("3.14");
        boolean startsWith = str.startsWith("Hello");
        boolean endsWith = str.endsWith("World!");

        // Substring
        String substring = str.substring(7, 12); // "World"
        String replaced = str.replace("World", "Java"); // "Hello, Java!"

        // Converting to Uppercase and Lowercase
        String uppercase = str.toUpperCase();
        String lowercase = str.toLowerCase();
    }
}
