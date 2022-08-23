package jonatan.andrei.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class FieldUtil {

    public static String findValue(String fieldName, Map<String, String> map, Class T) {
        try {
            return map.get(fieldName);
        } catch (Exception e) {
            log.error("Error when fetching field: " + fieldName, e);
            return null;
        }
    }
}
