package searchengine.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {

    private final Map<String, Set<Integer>> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }

    public void add(String word, int documentId) {

        Set<Integer> documents = index.get(word);

        if (documents == null) {
            documents = new HashSet<>();
            index.put(word, documents);
        }

        documents.add(documentId);
    }

    public Set<Integer> search(String word) {
        return index.getOrDefault(word, Set.of());
    }
}