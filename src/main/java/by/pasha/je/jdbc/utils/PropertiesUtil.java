package by.pasha.je.jdbc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    private static void loadProperties() {
        try (InputStream inputSteeam = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputSteeam);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
