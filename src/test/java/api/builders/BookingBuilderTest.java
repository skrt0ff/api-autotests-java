package api.builders;

import api.models.booking.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookingBuilderTest {

    @Test
    void withLastnameOverridesOnlyLastname() {
        Booking booking = BookingBuilder.aBooking().withLastname("Ibragim").build();

        assertEquals("Ibragim", booking.lastname());
        assertEquals("TestFN", booking.firstname());
    }

    @Test
    void defaultBookingHasDefaultValues() {
        Booking booking = BookingBuilder.aBooking().build();

        assertEquals("TestFN", booking.firstname());
        assertEquals("TestLN", booking.lastname());
        assertEquals(1, booking.totalprice());
        assertFalse(booking.depositpaid());
        assertNotNull(booking.bookingdates());
    }
}