package freewind.colablog.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemConfig {

    private List<String> blogDirectories = new ArrayList<>();

    public List<String> getBlogDirectories() {
        return blogDirectories;
    }

    public void setBlogDirectories(List<String> blogDirectories) {
        this.blogDirectories = blogDirectories;
    }

    public void addNewBlogRoot(File selectedDirectory) {
        String path = selectedDirectory.getPath();
        if (!blogDirectories.contains(path)) {
            blogDirectories.add(path);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
