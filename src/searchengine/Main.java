package searchengine;

import searchengine.crawler.Crawler;
import searchengine.index.Indexer;
import searchengine.index.InvertedIndex;

import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Crawl pages
        Crawler crawler = new Crawler();
        Set<String> pages = crawler.crawl("java.html");

        System.out.println("Crawled pages:");
        System.out.println(pages);

        // 2. Create index
        InvertedIndex invertedIndex = new InvertedIndex();
        Indexer indexer = new Indexer(invertedIndex);

        // 3. Index every crawled page
        for (String page : pages) {
            indexer.index(page);
        }

        // 4. Test searches
        System.out.println("\nSearch results:");

        System.out.println("java: " +
                invertedIndex.search("java"));

        System.out.println("programming: " +
                invertedIndex.search("programming"));

        System.out.println("database: " +
                invertedIndex.search("database"));

        System.out.println("network: " +
                invertedIndex.search("network"));
    }
}