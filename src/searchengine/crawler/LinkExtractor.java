package searchengine.crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkExtractor {

    public Set<String> extractLinks(Path htmlFile) throws IOException {

        Set<String> links = new HashSet<>();

        String html = Files.readString(htmlFile);

        Pattern pattern = Pattern.compile("href=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            links.add(matcher.group(1));
        }

        return links;
    }
}