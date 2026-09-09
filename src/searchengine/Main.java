package searchengine;

import searchengine.crawler.Crawler;
import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;
import searchengine.query.QueryEngine;

import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Crawl pages
        Crawler crawler = new Crawler();

        Set<String> pages = crawler.crawl("java.html");

        System.out.println("Crawled pages:");
        System.out.println(pages);

        // 2. Create inverted index
        InvertedIndex invertedIndex = new InvertedIndex();

        // 3. Create indexer
        Indexer indexer = new Indexer(invertedIndex);

        // 4. Index every crawled page
        for (String page : pages) {
            indexer.index(page);
        }

        // 5. Create query engine
        QueryEngine queryEngine =
                new QueryEngine(invertedIndex, indexer);

        // 6. Search
        System.out.println("\nSearch results:");

        System.out.println("java: " +
                queryEngine.search("java"));

        System.out.println("programming: " +
                queryEngine.search("programming"));

        System.out.println("database: " +
                queryEngine.search("database"));

        System.out.println("network: " +
                queryEngine.search("network"));
    }
}