package api.tests;

import api.clients.PostsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostsApiTest {

    @Test
    void getPostByIdReturnsRequestedPost() {
        Response response = new PostsApi().getById(1);
        assertEquals(200, response.getStatusCode());
        assertEquals(1, response.jsonPath().getInt("id"));
    }

    @Test
    void getNonExistingPostReturns404() {
        Response response = new PostsApi().getById(99999);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void getAllPostsReturns100Posts() {
        Response response = new PostsApi().getAll();
        assertEquals(200, response.getStatusCode());
        assertEquals(100, response.jsonPath().getList("id").size());
    }
}
