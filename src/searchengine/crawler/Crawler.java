package searchengine.crawler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Crawler {

    private final Queue<String> pending;
    private final Set<String> visited;
    private final LinkExtractor linkExtractor;

    public Crawler() {
        pending = new ArrayDeque<>();
        visited = new HashSet<>();
        linkExtractor = new LinkExtractor();
    }

    public Set<String> crawl(String startPage) throws IOException {

        pending.add(startPage);

        while (!pending.isEmpty()) {

            String currentPage = pending.poll();

            if (visited.contains(currentPage)) {
                continue;
            }

            visited.add(currentPage);

            Path pagePath = Path.of("Pages", currentPage);

            Set<String> links = linkExtractor.extractLinks(pagePath);

            for (String link : links) {
                if (!visited.contains(link)) {
                    pending.add(link);
                }
            }
        }

        return visited;
    }
}