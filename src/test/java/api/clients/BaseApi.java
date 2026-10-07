package api.clients;

import api.logging.TestLogger;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
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

    // Общая точка отправки: лог, запрос, лог статуса.
    // Тело передаём отдельно: при склейке спецификаций оно теряется.
    private Response send(Method method, String path, RequestSpecification extraSpec, Object body) {
        logger.info(method + " " + path);

        RequestSpecification request = given()
                .spec(extraSpec)
                .spec(requestSpec());

        if (body != null) {
            request.body(body);
        }

        Response response = request
                .when()
                .request(method, path);

        logger.info("Status: " + response.getStatusCode());
        return response;
    }

    // Общее для всех запросов. Применяется последним, чтобы не потерять адрес сервера.
    // Accept строкой: список значений вызывает 418 у Restful-Booker.
    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl())
                .setAccept("application/json")
                .build();
    }

    public Response getAll() {
        return sendGet(basePath());
    }

    public Response getById(int id) {
        return sendGet(basePath() + "/" + id);
    }

    private Response sendGet(String path) {
        return send(Method.GET, path, new RequestSpecBuilder().build(), null);
    }

    protected Response sendPost(String path, Object body) {
        RequestSpecification spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON.withCharset("UTF-8"))
                .build();

        return send(Method.POST, path, spec, body);
    }

    protected Response sendPut(String path, Object body, String token) {
        RequestSpecification spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON.withCharset("UTF-8"))
                .addCookie("token", token)
                .build();

        return send(Method.PUT, path, spec, body);
    }

    protected Response sendPatch(String path, Object body, String token) {
        RequestSpecification spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON.withCharset("UTF-8"))
                .addCookie("token", token)
                .build();

        return send(Method.PATCH, path, spec, body);
    }

    protected Response sendDelete(String path, String token) {
        RequestSpecification spec = new RequestSpecBuilder()
                .addCookie("token", token)
                .build();

        return send(Method.DELETE, path, spec, null);
    }
}