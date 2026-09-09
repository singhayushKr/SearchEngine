package searchengine.ranking;

import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;
import searchengine.model.SearchResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Ranker {

    private final InvertedIndex invertedIndex;
    private final Indexer indexer;

    public Ranker(
            InvertedIndex invertedIndex,
            Indexer indexer
    ) {
        this.invertedIndex = invertedIndex;
        this.indexer = indexer;
    }

    public List<SearchResult> rank(
            Set<Integer> documentIds,
            String[] queryTerms
    ) {

        List<SearchResult> results = new ArrayList<>();

        int totalDocuments =
                invertedIndex.getDocumentCount();

        for (int documentId : documentIds) {

            double score = 0.0;

            for (String term : queryTerms) {

                Map<Integer, Integer> matches =
                        invertedIndex.search(term);

                Integer frequency =
                        matches.get(documentId);

                if (frequency == null) {
                    continue;
                }

                int documentFrequency =
                        matches.size();

                // Logarithmic Term Frequency
                double tf =
                        1.0 + Math.log(frequency);

                // Inverse Document Frequency
                double idf = Math.log(
                        (1.0 + totalDocuments)
                                / (1.0 + documentFrequency)
                ) + 1.0;

                // TF-IDF
                score += tf * idf;
            }

            String path =
                    indexer.getDocumentPath(documentId);

            results.add(
                    new SearchResult(
                            documentId,
                            path,
                            score
                    )
            );
        }

        // Highest score first
        results.sort(
                Comparator.comparingDouble(
                        SearchResult::getScore
                ).reversed()
        );

        return results;
    }
}