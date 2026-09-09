package searchengine.index;

import java.util.HashMap;
import java.util.Map;

public class InvertedIndex {

    private final Map<String, Map<Integer, Integer>> index;
    private int documentCount;

    public InvertedIndex() {
        index = new HashMap<>();
        documentCount = 0;
    }

    public void add(String word, int documentId) {

        Map<Integer, Integer> documents =
                index.computeIfAbsent(word, key -> new HashMap<>());

        documents.merge(documentId, 1, Integer::sum);
    }

    public Map<Integer, Integer> search(String word) {
        return index.getOrDefault(word, Map.of());
    }

    public void addDocument() {
        documentCount++;
    }

    public int getDocumentCount() {
        return documentCount;
    }
}