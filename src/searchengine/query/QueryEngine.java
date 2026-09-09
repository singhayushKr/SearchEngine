package searchengine.query;

import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QueryEngine {

    private final InvertedIndex invertedIndex;
    private final Indexer indexer;

    public QueryEngine(InvertedIndex invertedIndex, Indexer indexer) {
        this.invertedIndex = invertedIndex;
        this.indexer = indexer;
    }

    public List<String> search(String query) {

        Set<Integer> documentIds = invertedIndex.search(
                query.toLowerCase()
        );

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