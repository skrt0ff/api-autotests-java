package api.clients;

import api.logging.TestLogger;

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
        return "https://restful-booker.herokuapp.com";
    }
}
