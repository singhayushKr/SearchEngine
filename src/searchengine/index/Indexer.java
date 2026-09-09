package searchengine.index;

import searchengine.model.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Indexer {

    private final InvertedIndex invertedIndex;
    private int documentId = 1;

    public Indexer(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public void index(String pagePath) throws IOException {

        Path path = Path.of("Pages", pagePath);

        String content = Files.readString(path);

        Document document = new Document(
                documentId++,
                pagePath,
                content
        );

        index(document);
    }

    public void index(Document document) {

        String content = document.getContent().toLowerCase();

        String[] words = content.split("\\W+");

        for (String word : words) {
            if (!word.isEmpty()) {
                invertedIndex.add(word, document.getId());
            }
        }
    }
}