package api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final String FILE_NAME = "config.properties";
    private static final Properties PROPERTIES = load();

    private Config() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        return value;
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (input == null) {
                throw new IllegalStateException(FILE_NAME + " not found in resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + FILE_NAME, e);
        }
        return properties;
    }
}