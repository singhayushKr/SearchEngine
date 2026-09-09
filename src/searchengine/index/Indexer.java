package searchengine.index;

import searchengine.model.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Indexer {

    private final InvertedIndex invertedIndex;
    private final Map<Integer, String> documentPaths;
    private int documentId = 1;

    public Indexer(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
        this.documentPaths = new HashMap<>();
    }

    public void index(String pagePath) throws IOException {

        Path path = Path.of("Pages", pagePath);

        String content = Files.readString(path);

        Document document = new Document(
                documentId,
                pagePath,
                content
        );

        documentPaths.put(documentId, pagePath);

        documentId++;

        index(document);
    }

    public void index(Document document) {

        String content = document.getContent();

        // Remove HTML tags
        content = content.replaceAll("<[^>]*>", " ");

        // Normalize case
        content = content.toLowerCase();

        // Split into words
        String[] words = content.split("\\W+");

        for (String word : words) {
            if (!word.isEmpty()) {
                invertedIndex.add(word, document.getId());
            }
        }
    }

    public String getDocumentPath(int documentId) {
        return documentPaths.get(documentId);
    }
}