package searchengine;

import searchengine.crawler.Crawler;

public class Main {

    public static void main(String[] args) {

        Crawler crawler = new Crawler();

        System.out.println(crawler.crawl("java.html"));
    }
}