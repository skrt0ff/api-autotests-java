package api.clients;

import api.logging.TestLogger;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public abstract class BaseApi {

    private final TestLogger logger;

    protected abstract String basePath();

    protected abstract String baseUrl();

    protected BaseApi(TestLogger logger) {
        this.logger = logger;
    }

    public Response getAll() {
        return sendGet(basePath());
    }

    public Response getById(int id) {
        return sendGet(basePath() + "/" + id);
    }

    private Response sendGet(String path) {
        logger.info("GET " + path);

        Response response = given()
                .baseUri(baseUrl())
                .when()
                .get(path);

        logger.info("Status: " + response.getStatusCode());
        return response;
    }
}
