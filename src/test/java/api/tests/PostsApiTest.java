package api.tests;

import api.clients.PostsApi;
import api.logging.ConsoleLogger;
import api.logging.InMemoryLogger;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("JSONPlaceholder")
@Feature("Посты")
public class PostsApiTest {

    private PostsApi postsApi;

    @BeforeEach
    void setUp() {
        postsApi = new PostsApi(new ConsoleLogger());
    }

    @Test
    @DisplayName("Структура поста соответствует схеме")
    void getPostMatchesSchema() {
        Response response = postsApi.getById(1);

        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/post.json"));
    }

    @Test
    void getPostByIdReturnsRequestedPost() {
        Response response = postsApi.getById(1);

        assertEquals(200, response.getStatusCode());
        assertEquals(1, response.jsonPath().getInt("id"));
    }

    @Test
    void getNonExistingPostReturns404() {
        Response response = postsApi.getById(99999);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    void getAllPostsReturns100Posts() {
        Response response = postsApi.getAll();

        assertEquals(200, response.getStatusCode());
        assertEquals(100, response.jsonPath().getList("id").size());
    }

    @Test
    void getByIdLogsRequestAndStatus() {
        InMemoryLogger logger = new InMemoryLogger();
        PostsApi api = new PostsApi(logger);

        api.getById(1);

        assertEquals(
                List.of("[INFO]: GET /posts/1", "[INFO]: Status: 200"),
                logger.getMessages());
    }
}