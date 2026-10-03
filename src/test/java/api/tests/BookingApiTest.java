package api.tests;

import api.clients.BookingApi;
import api.logging.ConsoleLogger;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingApiTest {

    private BookingApi bookingApi;

    @BeforeEach
    void setUp() {
        bookingApi = new BookingApi(new ConsoleLogger());
    }

    @Test
    void getAllBookingsReturns200AndNotEmptyList() {
        Response response = bookingApi.getAll();

        assertEquals(200, response.getStatusCode());
        assertFalse(response.jsonPath().getList("bookingid").isEmpty());
    }

    @Test
    void getBookingByIdReturnsBooking() {
        int id = bookingApi.getAll().jsonPath().getInt("[0].bookingid");

        Response response = bookingApi.getById(id);

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("firstname"));
    }
}