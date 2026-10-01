package api.models;

public class Post {
    private final int userId;
    private final int id;
    private final String title;
    private final String body;

    public Post(int userId, int id, String title, String body) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or blank");
        }

        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("body cannot be null or blank");
        }

        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

}
