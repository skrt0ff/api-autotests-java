package api.clients;

import api.logging.TestLogger;
import api.specs.RequestSpecs;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
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
    // specs применяются по очереди; общая requestSpec() идёт последней, чтобы адрес сервера
    // не перезаписался. Тело добавляется отдельно: при склейке спецификаций оно теряется.
    private Response send(Method method, String path, Object body, RequestSpecification... specs) {
        logger.info(method + " " + path);

        RequestSpecification request = given();
        for (RequestSpecification spec : specs) {
            request.spec(spec);
        }
        request.spec(requestSpec());

        if (body != null) {
            request.body(body);
        }

        Response response = request
                .when()
                .request(method, path);

        logger.info("Status: " + response.getStatusCode());
        return response;
    }

    // Accept строкой: список значений вызывает 418 у Restful-Booker.
    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl())
                .setAccept("application/json")
                .addFilter(new AllureRestAssured())
                .build();
    }

    public Response getAll() {
        return sendGet(basePath());
    }

    public Response getById(int id) {
        return sendGet(basePath() + "/" + id);
    }

    private Response sendGet(String path) {
        return send(Method.GET, path, null, RequestSpecs.empty());
    }

    protected Response sendPost(String path, Object body) {
        return send(Method.POST, path, body, RequestSpecs.json());
    }

    protected Response sendPut(String path, Object body, String token) {
        return send(Method.PUT, path, body, RequestSpecs.json(), RequestSpecs.authorized(token));
    }

    protected Response sendPatch(String path, Object body, String token) {
        return send(Method.PATCH, path, body, RequestSpecs.json(), RequestSpecs.authorized(token));
    }

    protected Response sendDelete(String path, String token) {
        return send(Method.DELETE, path, null, RequestSpecs.authorized(token));
    }
}