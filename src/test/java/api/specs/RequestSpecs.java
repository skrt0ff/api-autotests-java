package api.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecs {

    private RequestSpecs() {
    }

    // Для запросов, которым нечего добавлять (GET)
    public static RequestSpecification empty() {
        return new RequestSpecBuilder().build();
    }

    // Для запросов с JSON-телом
    public static RequestSpecification json() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON.withCharset("UTF-8"))
                .build();
    }

    // Для запросов, которым нужен токен (PUT, PATCH, DELETE)
    public static RequestSpecification authorized(String token) {
        return new RequestSpecBuilder()
                .addCookie("token", token)
                .build();
    }
}