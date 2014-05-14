package freewind.colablog.models;

import com.google.common.collect.Lists;
import freewind.colablog.utils.IO;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Article {

    public static final String SEPARATION_LINE = "-----";

    private final File file;
    private final LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    private String content;

    public Article(File file) {
        this.file = file;
        init();
    }

    private void init() {
        String content = IO.readFileToString(getFile()).trim();
        if (content.startsWith(SEPARATION_LINE)) {
            List<String> lines = IO.readLines(content);
            List<String> propertyLines = takePropertyLines(lines);
            propertyLines.stream()
                    .filter(line -> line.contains(":"))
                    .forEach(line -> {
                        String key = StringUtils.substringBefore(line, ":");
                        String value = StringUtils.substringAfter(line, ":");
                        properties.put(key.trim(), value.trim());
                    });

            this.content = StringUtils.join(lines, "\n");
        } else {
            this.content = content;
        }
    }

    private List<String> takePropertyLines(List<String> lines) {
        int propertiesEndIndex = findPropertyEndIndex(lines);
        return takePropertyLines(lines, propertiesEndIndex);
    }

    private LinkedList<String> takePropertyLines(List<String> lines, int propertiesEndIndex) {
        LinkedList<String> propertiesPart = Lists.newLinkedList();
        if (propertiesEndIndex > 0) {
            for (int i = 0; i <= propertiesEndIndex; i++) {
                propertiesPart.add(lines.remove(0));
            }
            propertiesPart.removeFirst();
            propertiesPart.removeLast();
        }
        return propertiesPart;
    }

    private int findPropertyEndIndex(List<String> lines) {
        int propertiesEndIndex = 0;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith(SEPARATION_LINE)) {
                propertiesEndIndex = i;
                break;
            }
        }
        return propertiesEndIndex;
    }

    public File getFile() {
        return file;
    }

    public String getTitle() {
        List<String> lines = IO.readLines(getContent());
        if (!lines.isEmpty()) {
            return lines.get(0);
        }
        return null;
    }

    public void setContent(String text) {
        this.content = text;
    }

    public String getContent() {
        return content.trim();
    }

    public String getFullContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATION_LINE);
        sb.append("\n");
        for (String key : properties.keySet()) {
            String value = properties.get(key);
            sb.append(key).append(": ").append(value).append("\n");
        }
        sb.append(SEPARATION_LINE);
        sb.append("\n");
        sb.append(content);
        return sb.toString();
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String getCreatedAt() {
        return getProperty("createdAt");
    }

    public String getUpdatedAt() {
        return getProperty("updatedAt");
    }

    public String getKeywords() {
        return getProperty("keywords");
    }

    public String getId() {
        return getProperty("id");
    }

    @Override
    public String toString() {
        return this.getTitle() + "\n" + this.file;
    }

    public String getFileName() {
        return getFile().getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Article) {
            return this.file.equals(((Article) obj).file);
        }
        return false;
    }
}
