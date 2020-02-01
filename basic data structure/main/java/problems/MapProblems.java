package problems;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Parts b.i and b.ii should go here.
 *
 * (Implement contains3 and intersect as described by the spec
 *  See the spec on the website for examples and more explanation!)
 */
public class MapProblems {

    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> result = new HashMap<>();
        for (String key1 : m1.keySet()) {
            for (String key2 : m2.keySet()) {
                if (key1.equals(key2) && m1.get(key1) == m2.get(key2)) {
                    result.put(key1, m1.get(key1));
                }
            }
        }
        return result;
    }

    public static boolean contains3(List<String> input) {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            if (!result.keySet().contains(input.get(i))) {
                result.put(input.get(i), 0);
            }
            result.put(input.get(i), result.get(input.get(i)) + 1);
        }
        for (String key : result.keySet()) {
            if (result.get(key) >= 3) {
                return true;
            }
        }
        return false;
    }
}
