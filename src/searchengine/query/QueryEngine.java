package searchengine.query;

import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;
import searchengine.model.SearchResult;
import searchengine.ranking.Ranker;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryEngine {

    private final InvertedIndex invertedIndex;
    private final Indexer indexer;
    private final Ranker ranker;

    public QueryEngine(
            InvertedIndex invertedIndex,
            Indexer indexer
    ) {
        this.invertedIndex = invertedIndex;
        this.indexer = indexer;
        this.ranker = new Ranker(
                invertedIndex,
                indexer
        );
    }

    public List<SearchResult> search(String query) {

        String normalizedQuery =
                query.toLowerCase().trim();

        if (normalizedQuery.isEmpty()) {
            return List.of();
        }

        // Phrase search
        if (normalizedQuery.startsWith("\"")
                && normalizedQuery.endsWith("\"")) {

            String phrase =
                    normalizedQuery.substring(
                            1,
                            normalizedQuery.length() - 1
                    ).trim();

            return searchPhrase(phrase);
        }

        // OR query
        if (normalizedQuery.contains(" or ")) {
            return searchOr(normalizedQuery);
        }

        // AND / normal query
        return searchAnd(normalizedQuery);
    }

    private List<SearchResult> searchAnd(String query) {

        String[] words =
                query.split("\\s+and\\s+|\\s+");

        Set<Integer> resultIds = null;

        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(word);

            if (resultIds == null) {

                resultIds =
                        new HashSet<>(matches.keySet());

            } else {

                resultIds.retainAll(
                        matches.keySet()
                );
            }
        }

        if (resultIds == null) {
            return List.of();
        }

        return ranker.rank(
                resultIds,
                words
        );
    }

    private List<SearchResult> searchOr(String query) {

        String[] words =
                query.split("\\s+or\\s+");

        Set<Integer> resultIds =
                new HashSet<>();

        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(
                            word.trim()
                    );

            resultIds.addAll(
                    matches.keySet()
            );
        }

        return ranker.rank(
                resultIds,
                words
        );
    }

    private List<SearchResult> searchPhrase(
            String phrase
    ) {

        String[] words =
                phrase.split("\\s+");

        if (words.length == 0) {
            return List.of();
        }

        // First find documents containing
        // every word in the phrase.
        Set<Integer> candidateIds = null;

        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(word);

            if (candidateIds == null) {

                candidateIds =
                        new HashSet<>(matches.keySet());

            } else {

                candidateIds.retainAll(
                        matches.keySet()
                );
            }
        }

        if (candidateIds == null
                || candidateIds.isEmpty()) {

            return List.of();
        }

        // Now verify the actual phrase.
        Set<Integer> phraseMatches =
                new HashSet<>();

        String normalizedPhrase =
                phrase.toLowerCase();

        for (int documentId : candidateIds) {

            String content =
                    indexer.getDocumentContent(
                            documentId
                    );

            if (content == null) {
                continue;
            }

            content = content
                    .replaceAll("<[^>]*>", " ")
                    .toLowerCase();

            if (content.contains(normalizedPhrase)) {
                phraseMatches.add(documentId);
            }
        }

        return ranker.rank(
                phraseMatches,
                words
        );
    }
}