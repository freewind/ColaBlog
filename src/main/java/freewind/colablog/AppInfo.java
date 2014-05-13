package freewind.colablog;

import freewind.colablog.structrue.BlogStructure;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AppInfo {

    private File currentBlogDir;

    public void setCurrentBlogDir(File currentBlogDir) {
        this.currentBlogDir = currentBlogDir;
    }

    public BlogStructure getBlogStructure() {
        if (currentBlogDir != null) {
            return new BlogStructure(currentBlogDir);
        }
        return null;
    }
}
