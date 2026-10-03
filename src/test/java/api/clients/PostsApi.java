package api.clients;

import api.config.Config;
import api.logging.TestLogger;

public class PostsApi extends BaseApi{

    public PostsApi(TestLogger logger) {
        super(logger);
    }

    @Override
    protected String basePath() {
        return "/posts";
    }

    @Override
    protected String baseUrl() {
        return Config.get("jsonplaceholder.baseUrl");
    }
}
