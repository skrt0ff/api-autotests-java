package api.tests;

import api.logging.ConsoleLogger;
import api.logging.InMemoryLogger;
import api.logging.TestLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoggerTest {

    private void doWork(TestLogger logger) {
        logger.info("Start");
        logger.error("Fail");
    }

    @Test
    void inMemoryLoggerStoresMessages() {

        InMemoryLogger logger = new InMemoryLogger();
        doWork(logger);

        Assertions.assertEquals(List.of("[INFO]: Start", "[ERROR]: Fail"), logger.getMessages());
    }

    @Test
    void consoleLoggerWorksWithSameMethod() {
        doWork(new ConsoleLogger());
    }

    @Test
    void messagesCannotBeModifiedFromOutside() {
        InMemoryLogger logger = new InMemoryLogger();
        logger.info("Start");

        assertThrows(UnsupportedOperationException.class,
                () -> logger.getMessages().add("hack"));
    }
}



