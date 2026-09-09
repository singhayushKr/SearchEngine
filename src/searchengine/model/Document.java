package searchengine.model;

public class Document {

    private final int id;
    private final String path;
    private final String content;

    public Document(int id, String path, String content) {
        this.id = id;
        this.path = path;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
