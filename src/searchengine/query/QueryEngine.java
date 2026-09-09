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
    private final QueryParser parser;

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

        this.parser = new QueryParser();
    }

    public List<SearchResult> search(String query) {

        QueryParser.Query parsedQuery =
                parser.parse(query);

        return switch (parsedQuery.getType()) {

            case EMPTY -> List.of();

            case AND ->
                    searchAnd(
                            parsedQuery.getTerms()
                    );

            case OR ->
                    searchOr(
                            parsedQuery.getTerms()
                    );

            case PHRASE ->
                    searchPhrase(
                            parsedQuery.getTerms()
                    );
        };
    }

    private List<SearchResult> searchAnd(
            String[] words
    ) {

        Set<Integer> resultIds = null;

        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(
                            word
                    );

            if (resultIds == null) {

                resultIds =
                        new HashSet<>(
                                matches.keySet()
                        );

            } else {

                resultIds.retainAll(
                        matches.keySet()
                );
            }
        }

        if (resultIds == null
                || resultIds.isEmpty()) {

            return List.of();
        }

        return ranker.rank(
                resultIds,
                words
        );
    }

    private List<SearchResult> searchOr(
            String[] words
    ) {

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

        if (resultIds.isEmpty()) {
            return List.of();
        }

        return ranker.rank(
                resultIds,
                words
        );
    }

    private List<SearchResult> searchPhrase(
            String[] words
    ) {

        Set<Integer> candidateIds = null;

        // First use the inverted index
        // to find documents containing
        // every word.
        for (String word : words) {

            Map<Integer, Integer> matches =
                    invertedIndex.search(
                            word
                    );

            if (candidateIds == null) {

                candidateIds =
                        new HashSet<>(
                                matches.keySet()
                        );

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

        // Build the actual phrase.
        String phrase =
                String.join(" ", words);

        Set<Integer> phraseMatches =
                new HashSet<>();

        for (int documentId : candidateIds) {

            String content =
                    indexer.getDocumentContent(
                            documentId
                    );

            if (content == null) {
                continue;
            }

            content = content
                    .replaceAll(
                            "<[^>]*>",
                            " "
                    )
                    .toLowerCase();

            if (content.contains(phrase)) {

                phraseMatches.add(
                        documentId
                );
            }
        }

        return ranker.rank(
                phraseMatches,
                words
        );
    }
}