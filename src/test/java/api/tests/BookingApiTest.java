package api.tests;

import api.clients.BaseApi;
import api.clients.BookingApi;
import api.logging.ConsoleLogger;
import api.models.booking.Booking;
import api.models.booking.BookingDates;
import api.models.booking.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        Booking booking = response.as(Booking.class);

        assertNotNull(booking.firstname());
        assertNotNull(booking.bookingdates());
    }

    @Test
    void createBookingReturnsCreatedBooking() {
        Booking booking = new Booking(
                "test",
                "test",
                1,
                true,
                new BookingDates("2026-10-10", "2026-10-15"),
                "test"
        );

        Response response = bookingApi.create(booking);
        assertEquals(200, response.getStatusCode());

        CreatedBooking createdBooking = response.as(CreatedBooking.class);
        assertTrue(createdBooking.bookingid() > 0);
        assertEquals(booking, createdBooking.booking());
    }

    @Test
    void getTokenReturnsNotEmptyToken() {
        String token = bookingApi.getToken();
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deleteBookingReturns201() {
        Booking booking = new Booking(
                "test2",
                "test2",
                2,
                false,
                new BookingDates("2026-10-10", "2026-10-15"),
                "test"
        );

        Response createResponse = bookingApi.create(booking);
        assertEquals(200, createResponse.getStatusCode());
        CreatedBooking createdBooking = createResponse.as(CreatedBooking.class);

        String token = bookingApi.getToken();

        Response deleteResponse = bookingApi.delete(createdBooking.bookingid(), token);
        assertEquals(201, deleteResponse.getStatusCode());
        Response getResponse = bookingApi.getById(createdBooking.bookingid());
        assertEquals(404, getResponse.getStatusCode());
    }
}