package freewind.colablog;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AppInfo {

    private File currentBlogDir;

    public File getCurrentBlogDir() {
        return currentBlogDir;
    }

    public void setCurrentBlogDir(File currentBlogDir) {
        this.currentBlogDir = currentBlogDir;
    }

}
