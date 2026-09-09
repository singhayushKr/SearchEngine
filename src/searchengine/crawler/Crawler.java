package searchengine.crawler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Crawler {

    private final Queue<String> pending;
    private final Set<String> visited;

    public Crawler() {
        pending = new ArrayDeque<>();
        visited = new HashSet<>();
    }

    public Set<String> crawl(String startPage) {

        pending.add(startPage);

        while (!pending.isEmpty()) {

            String currentPage = pending.poll();

            if (visited.contains(currentPage)) {
                continue;
            }

            visited.add(currentPage);

            // TODO: Read currentPage and discover links
        }

        return visited;
    }
}