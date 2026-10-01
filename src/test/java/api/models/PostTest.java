package api.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostTest {

    @Test
    void postStoresAllFields() {
        Post post = new Post(1,1,"Заголовок","Текст");
        assertEquals(1, post.getUserId());
        assertEquals(1, post.getId());
        assertEquals("Заголовок", post.getTitle());
        assertEquals("Текст", post.getBody());
    }

    @Test
    void nullTitleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Post(1,1, null, "Текст");
        });
    }

    @Test
    void emptyTitleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Post(1,1, "", "Текст");
        });
    }

    @Test
    void blankTitleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Post(1,1, "  ", "Текст");
        });
    }

    @Test
    void nullBodyIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Post(1,1, "Заголовок", null);
        });
    }

    @Test
    void emptyBodyIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Post(1,1, "Заголовок", "");
        });
    }

    @Test
    void blankBodyIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Post(1,1, "Заголовок", "  ");
        });
    }
}
