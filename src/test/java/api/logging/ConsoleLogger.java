package api.logging;

public class ConsoleLogger implements TestLogger {

    @Override
    public void info(String message) {
        System.out.println("[INFO]: " + message);
    }

    @Override
    public void error(String message) {
        System.out.println("[ERROR]: " + message);
    }
}
