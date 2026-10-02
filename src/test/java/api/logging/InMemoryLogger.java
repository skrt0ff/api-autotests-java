package api.logging;

import java.util.ArrayList;
import java.util.List;

public class InMemoryLogger implements TestLogger {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void info(String message) {
        messages.add("[INFO]: " + message);
    }

    @Override
    public void error(String message) {
        messages.add("[ERROR]: " + message);
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }
}
