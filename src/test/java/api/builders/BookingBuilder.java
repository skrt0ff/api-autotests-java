package api.builders;

import api.models.booking.Booking;
import api.models.booking.BookingDates;

public class BookingBuilder {
    private String firstname = "TestFN";
    private String lastname = "TestLN";
    private int totalprice = 1;
    private boolean depositpaid = false;
    private BookingDates bookingdates = new BookingDates("2026-10-10", "2026-10-15");
    private String additionalneeds = "Test";

    private BookingBuilder() {
    }

    public static BookingBuilder aBooking() {
        return new BookingBuilder();
    }

    public BookingBuilder withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public BookingBuilder withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public BookingBuilder withTotalprice(int totalprice) {
        this.totalprice = totalprice;
        return this;
    }

    public BookingBuilder withDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
        return this;
    }

    public BookingBuilder withBookingdates(BookingDates bookingdates) {
        this.bookingdates = bookingdates;
        return this;
    }

    public BookingBuilder withAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
        return this;
    }

    public Booking build() {
        return new Booking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
    }
}