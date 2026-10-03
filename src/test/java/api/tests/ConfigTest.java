package api.tests;

import api.config.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @Test
    void existingKeyReturnsValue() {
        String url = Config.get("jsonplaceholder.baseUrl");

        assertFalse(url.isBlank());
    }

    @Test
    void missingKeyThrowsException() {
        assertThrows(IllegalStateException.class,
                () -> Config.get("no.such.key"));
    }
}
