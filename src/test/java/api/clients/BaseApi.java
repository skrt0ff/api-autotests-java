package api.clients;

import api.logging.TestLogger;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApi {

    private final TestLogger logger;

    protected abstract String basePath();

    protected abstract String baseUrl();

    protected BaseApi(TestLogger logger) {
        this.logger = logger;
    }

    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl())
                .setAccept(ContentType.JSON)
                .build();
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
                .spec(requestSpec())
                .when()
                .get(path);

        logger.info("Status: " + response.getStatusCode());
        return response;
    }

    protected Response sendPost(String path, Object body) {
        logger.info("POST " + path);

        Response response = given()
                .spec(requestSpec())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path);

        logger.info("Status: " + response.getStatusCode());
        return response;
    }

    protected Response sendDelete(String path, String token) {
        logger.info("DELETE " + path);

        Response response = given()
                .spec(requestSpec())
                .cookie("token", token)
                .when()
                .delete(path);

        logger.info("Status: " + response.getStatusCode());
        return response;
    }
}
