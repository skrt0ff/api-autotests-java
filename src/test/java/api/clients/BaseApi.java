package api.clients;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public abstract class BaseApi {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    protected abstract String basePath();

    public Response getAll() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(basePath());
    }

    public Response getById(int id) {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(basePath() + "/" + id);
    }
}
