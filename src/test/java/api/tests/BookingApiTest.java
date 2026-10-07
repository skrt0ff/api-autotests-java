package api.tests;

import api.builders.BookingBuilder;
import api.clients.BookingApi;
import api.logging.ConsoleLogger;
import api.models.booking.Booking;
import api.models.booking.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookingApiTest {

    private BookingApi bookingApi;
    private Integer createdBookingId;

    @BeforeEach
    void setUp() {
        bookingApi = new BookingApi(new ConsoleLogger());
    }

    @AfterEach
    void cleanUp() {
        if (createdBookingId != null) {
            String token = bookingApi.getToken();
            bookingApi.delete(createdBookingId, token);
        }
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
        Booking booking = BookingBuilder.aBooking().withLastname("Ibragim").build();

        Response response = bookingApi.create(booking);
        assertEquals(200, response.getStatusCode());

        CreatedBooking createdBooking = response.as(CreatedBooking.class);
        createdBookingId = createdBooking.bookingid();
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
        Booking booking = BookingBuilder.aBooking().withFirstname("Kim").build();

        Response createResponse = bookingApi.create(booking);
        assertEquals(200, createResponse.getStatusCode());
        CreatedBooking createdBooking = createResponse.as(CreatedBooking.class);

        String token = bookingApi.getToken();

        Response deleteResponse = bookingApi.delete(createdBooking.bookingid(), token);
        assertEquals(201, deleteResponse.getStatusCode());
        Response getResponse = bookingApi.getById(createdBooking.bookingid());
        assertEquals(404, getResponse.getStatusCode());
    }

    @Test
    void updateBookingReplacesData() {
        Booking originalBooking = BookingBuilder.aBooking()
                .withFirstname("Kim")
                .withLastname("Kim")
                .build();

        Response createResponse = bookingApi.create(originalBooking);
        assertEquals(200, createResponse.getStatusCode());

        CreatedBooking createdBooking = createResponse.as(CreatedBooking.class);
        createdBookingId = createdBooking.bookingid();

        String token = bookingApi.getToken();

        Booking newBooking = BookingBuilder.aBooking()
                .withFirstname("Update")
                .withLastname("Update")
                .build();

        Response updateResponse = bookingApi.update(createdBookingId, newBooking, token);
        assertEquals(200, updateResponse.getStatusCode());

        Booking updated = updateResponse.as(Booking.class);
        assertEquals(newBooking, updated);

        Booking saved = bookingApi.getById(createdBookingId).as(Booking.class);
        assertEquals(newBooking, saved);
    }

    @Test
    void partialUpdateChangesOnlyGivenField() {
        Booking originalBooking = BookingBuilder.aBooking()
                .withFirstname("Kim")
                .withLastname("Kim")
                .withTotalprice(1)
                .build();

        Response createResponse = bookingApi.create(originalBooking);
        assertEquals(200, createResponse.getStatusCode());

        CreatedBooking createdBooking = createResponse.as(CreatedBooking.class);
        createdBookingId = createdBooking.bookingid();

        String token = bookingApi.getToken();
        Response partialUpdateResponse = bookingApi.partialUpdate(createdBookingId, Map.of("firstname", "Rodion"), token);
        assertEquals(200, partialUpdateResponse.getStatusCode());

        Booking updated = partialUpdateResponse.as(Booking.class);
        assertEquals("Rodion", updated.firstname());
        assertEquals(createdBooking.booking().lastname(), updated.lastname());
        assertEquals(createdBooking.booking().totalprice(), updated.totalprice());
    }

}