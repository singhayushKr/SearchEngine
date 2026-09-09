package searchengine;

import searchengine.crawler.Crawler;
import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;
import searchengine.model.SearchResult;
import searchengine.query.QueryEngine;

import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Crawl pages
        Crawler crawler = new Crawler();

        Set<String> pages =
                crawler.crawl("java.html");

        System.out.println("Crawled pages:");
        System.out.println(pages);

        // 2. Create inverted index
        InvertedIndex invertedIndex =
                new InvertedIndex();

        // 3. Create indexer
        Indexer indexer =
                new Indexer(invertedIndex);

        // 4. Index every crawled page
        for (String page : pages) {
            indexer.index(page);
        }

        // 5. Create query engine
        QueryEngine queryEngine =
                new QueryEngine(
                        invertedIndex,
                        indexer
                );

        // 6. Search
        System.out.println("\nSearch results:");

        printResults(
                "java",
                queryEngine.search("java")
        );

        printResults(
                "programming",
                queryEngine.search("programming")
        );

        printResults(
                "database",
                queryEngine.search("database")
        );

        printResults(
                "java programming",
                queryEngine.search("java programming")
        );

        printResults(
                "java OR database",
                queryEngine.search("java OR database")
        );

        printResults(
                "\"java programming\"",
                queryEngine.search("\"java programming\"")
        );

        printResults(
                "\"networking and java\"",
                queryEngine.search("\"networking and java\"")
        );
    }

    private static void printResults(
            String query,
            List<SearchResult> results
    ) {

        System.out.println(
                "\nQuery: " + query
        );

        for (SearchResult result : results) {

            System.out.println(
                    result.getPath()
                            + " -> score = "
                            + result.getScore()
            );
        }
    }
}