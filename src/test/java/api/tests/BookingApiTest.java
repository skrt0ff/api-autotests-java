package api.tests;

import api.builders.BookingBuilder;
import api.config.Config;
import api.models.auth.AuthRequest;
import api.models.booking.Booking;
import api.models.booking.CreatedBooking;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Restful-Booker")
@Feature("Бронирования")
public class BookingApiTest extends BaseBookingTest {

    @Test
    @DisplayName("Список броней возвращается и не пустой")
    void getAllBookingsReturns200AndNotEmptyList() {
        Response response = bookingApi.getAll();

        assertEquals(200, response.getStatusCode());
        assertFalse(response.jsonPath().getList("bookingid").isEmpty());
    }

    @Test
    @DisplayName("Бронь по id возвращается с заполненными данными")
    void getBookingByIdReturnsBooking() {
        int id = bookingApi.getAll().jsonPath().getInt("[0].bookingid");

        Response response = bookingApi.getById(id);
        assertEquals(200, response.getStatusCode());

        Booking booking = response.as(Booking.class);

        assertNotNull(booking.firstname());
        assertNotNull(booking.bookingdates());
    }

    @Test
    @DisplayName("Создание брони возвращает id и те же данные, что отправили")
    void createBookingReturnsCreatedBooking() {
        Booking booking = BookingBuilder.aBooking().withLastname("Ibragim").build();

        Response response = bookingApi.create(booking);
        assertEquals(200, response.getStatusCode());

        CreatedBooking createdBooking = response.as(CreatedBooking.class);
        registerForCleanup(createdBooking.bookingid());
        assertTrue(createdBooking.bookingid() > 0);
        assertEquals(booking, createdBooking.booking());
    }

    @Test
    @DisplayName("Токен авторизации выдаётся и не пустой")
    void getTokenReturnsNotEmptyToken() {
        String token = bookingApi.getToken();

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("Удаление брони возвращает 201, после чего бронь не находится")
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

    @ParameterizedTest(name = "токен: [{0}]")
    @DisplayName("Удаление с неверным токеном возвращает 403, бронь остаётся")
    @ValueSource(strings = {"", "invalid", "0123"})
    void deleteWithoutValidTokenReturns403(String invalidToken) {
        CreatedBooking createdBooking = createBooking(BookingBuilder.aBooking().build());
        int id = createdBooking.bookingid();

        Response response = bookingApi.delete(id, invalidToken);
        assertEquals(403, response.getStatusCode());
        assertEquals(200, bookingApi.getById(id).getStatusCode());
    }

    @Test
    @DisplayName("Несуществующая бронь возвращает 404")
    void getNonExistingBookingReturns404() {
        Response response = bookingApi.getById(Integer.MAX_VALUE);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    @DisplayName("Неверный пароль: 200 и причина «Bad credentials», токена нет")
    void authWithBadCredentialsReturnsReason() {
        AuthRequest badRequest = new AuthRequest(
                Config.get("restfulbooker.username"),
                "wrong-password");

        Response response = bookingApi.auth(badRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("Bad credentials", response.jsonPath().getString("reason"));

        assertNull(response.jsonPath().getString("token"));
    }

    @Test
    @DisplayName("Полное обновление (PUT) заменяет все данные брони")
    void updateBookingReplacesData() {
        Booking originalBooking = BookingBuilder.aBooking()
                .withFirstname("Kim")
                .withLastname("Kim")
                .build();
        CreatedBooking createdBooking = createBooking(originalBooking);

        String token = bookingApi.getToken();

        Booking newBooking = BookingBuilder.aBooking()
                .withFirstname("Update")
                .withLastname("Update")
                .withTotalprice(999)
                .build();

        Response updateResponse = bookingApi.update(createdBooking.bookingid(), newBooking, token);
        assertEquals(200, updateResponse.getStatusCode());

        Booking updated = updateResponse.as(Booking.class);
        assertEquals(newBooking, updated);

        Booking saved = bookingApi.getById(createdBooking.bookingid()).as(Booking.class);
        assertEquals(newBooking, saved);
    }

    @Test
    @DisplayName("Частичное обновление (PATCH) меняет только переданное поле")
    void partialUpdateChangesOnlyGivenField() {
        Booking originalBooking = BookingBuilder.aBooking()
                .withFirstname("Kim")
                .withLastname("Kim")
                .withTotalprice(1)
                .build();
        CreatedBooking createdBooking = createBooking(originalBooking);

        String token = bookingApi.getToken();

        Response patchResponse = bookingApi.partialUpdate(
                createdBooking.bookingid(), Map.of("firstname", "Rodion"), token);
        assertEquals(200, patchResponse.getStatusCode());

        Booking updated = patchResponse.as(Booking.class);
        assertEquals("Rodion", updated.firstname());
        assertEquals(createdBooking.booking().lastname(), updated.lastname());
        assertEquals(createdBooking.booking().totalprice(), updated.totalprice());
    }
}