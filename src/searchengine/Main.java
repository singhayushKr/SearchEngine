package searchengine;

import searchengine.crawler.Crawler;
import searchengine.crawler.LinkExtractor;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {
        Crawler crawler=new Crawler();
        System.out.println(crawler.crawl("java.html"));
//        LinkExtractor extractor = new LinkExtractor();


//        System.out.println(extractor.extractLinks(Path.of("Pages/java.html")));
    }
}