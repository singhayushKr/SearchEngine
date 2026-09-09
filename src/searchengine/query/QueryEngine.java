package searchengine.query;

import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryEngine {

    private final InvertedIndex invertedIndex;
    private final Indexer indexer;

    public QueryEngine(InvertedIndex invertedIndex, Indexer indexer) {
        this.invertedIndex = invertedIndex;
        this.indexer = indexer;
    }

    public List<String> search(String query) {

        String normalizedQuery = query.toLowerCase().trim();

        if (normalizedQuery.isEmpty()) {
            return List.of();
        }

        if (normalizedQuery.contains(" or ")) {
            return searchOr(normalizedQuery);
        }

        return searchAnd(normalizedQuery);
    }

    private List<String> searchAnd(String query) {

        String[] words = query.split("\\s+and\\s+|\\s+");

        Set<Integer> resultIds = null;

        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(word);

            if (resultIds == null) {
                resultIds = new HashSet<>(matches.keySet());
            } else {
                resultIds.retainAll(matches.keySet());
            }
        }

        if (resultIds == null) {
            return List.of();
        }

        return convertToPaths(resultIds);
    }

    private List<String> searchOr(String query) {

        String[] words = query.split("\\s+or\\s+");

        Set<Integer> resultIds = new HashSet<>();

        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(word.trim());

            resultIds.addAll(matches.keySet());
        }

        return convertToPaths(resultIds);
    }

    private List<String> convertToPaths(Set<Integer> documentIds) {

        List<String> results = new ArrayList<>();

        for (int documentId : documentIds) {

            String path = indexer.getDocumentPath(documentId);

            if (path != null) {
                results.add(path);
            }
        }

        return results;
    }
}