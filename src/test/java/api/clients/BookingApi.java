package api.clients;

import api.config.Config;
import api.logging.TestLogger;
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

    public Response create(Booking booking) {
        return sendPost(basePath(), booking);
    }
}
