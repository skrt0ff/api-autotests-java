package api.clients;

import api.config.Config;
import api.logging.TestLogger;
import api.models.auth.AuthRequest;
import api.models.auth.AuthResponse;
import api.models.booking.Booking;
import io.restassured.response.Response;

public class BookingApi extends BaseApi{

    public BookingApi(TestLogger logger) {
        super(logger);
    }

    @Override
    protected String basePath() {
        return "/booking";
    }

    @Override
    protected String baseUrl() {
        return Config.get("restfulbooker.baseUrl");
    }

    public String getToken() {
        AuthRequest request = new AuthRequest(
                Config.get("restfulbooker.username"),
                Config.get("restfulbooker.password")
        );

        Response response = sendPost("/auth", request);

        return response.as(AuthResponse.class).token();
    }

    public Response create(Booking booking) {
        return sendPost(basePath(), booking);
    }

    public Response delete(int id, String token) {
        return sendDelete(basePath() + "/" + id, token);
    }

}
