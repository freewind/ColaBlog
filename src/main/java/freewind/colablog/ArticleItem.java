package freewind.colablog;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ArticleItem {

    private final String filePath;
    private final String title;

    public ArticleItem(String filePath, String title) {
        this.filePath = filePath;
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return new File(filePath);
    }

    public String getContent() throws IOException {
        return FileUtils.readFileToString(getFile(), "UTF-8");
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return this.title + "\n" + this.filePath;
    }
}
