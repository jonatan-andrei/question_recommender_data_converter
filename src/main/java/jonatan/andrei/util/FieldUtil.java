package jonatan.andrei.util;

import io.quarkus.logging.Log;

import java.util.Map;

public class FieldUtil {

    public static String findValue(String fieldName, Map<String, String> map, Class T) {
        try {
            return map.get(fieldName);
        } catch (Exception e) {
            Log.error("Error when fetching field: " + fieldName, e);
            return null;
        }
    }
}
