package searchengine.model;

public class SearchResult {

    private final int documentId;
    private final String path;
    private final double score;

    public SearchResult(
            int documentId,
            String path,
            double score
    ) {
        this.documentId = documentId;
        this.path = path;
        this.score = score;
    }

    public int getDocumentId() {
        return documentId;
    }

    public String getPath() {
        return path;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return path + " (score=" + score + ")";
    }
}