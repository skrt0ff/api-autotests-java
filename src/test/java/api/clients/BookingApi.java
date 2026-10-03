package api.clients;

import api.config.Config;
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
        return Config.get("restfulbooker.baseUrl");
    }
}
