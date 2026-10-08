package api.tests;

import api.builders.BookingBuilder;
import api.clients.BookingApi;
import api.logging.ConsoleLogger;
import api.models.booking.Booking;
import api.models.booking.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseBookingTest {

    protected BookingApi bookingApi;
    private final List<Integer> createdIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        bookingApi = new BookingApi(new ConsoleLogger());
    }

    @AfterEach
    void cleanUp() {
        if (createdIds.isEmpty()) {
            return;
        }

        String token = bookingApi.getToken();
        for (int id : createdIds) {
            bookingApi.delete(id, token);
        }
    }

    protected CreatedBooking createBooking(Booking booking) {
        Response createResponse = bookingApi.create(booking);
        assertEquals(200, createResponse.getStatusCode());

        CreatedBooking createdBooking = createResponse.as(CreatedBooking.class);
        registerForCleanup(createdBooking.bookingid());
        return createdBooking;
    }

    protected void registerForCleanup(int id) {
        createdIds.add(id);
    }
}
