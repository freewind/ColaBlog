package freewind.colablog.common;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WordCounter {
    private final Pattern pattern = Pattern.compile("[\u00ff-\uffff]|\\S+");

    public int count(String text) {
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
