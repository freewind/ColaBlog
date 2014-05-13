package freewind.colablog.common;

import org.markdown4j.Markdown4jProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MarkdownConverter {

    public String toHtml(String markdown) {
        try {
            return new Markdown4jProcessor().process(markdown);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
