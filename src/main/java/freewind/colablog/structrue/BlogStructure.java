package freewind.colablog.structrue;

import java.io.File;

/**
 * root
 * .. - articles
 * .... - 2012-01-02-a-blog.md
 * .... - 2012-01-02-a-blog.md
 * .... - 2012-01-02-a-blog.md
 * .. - templates
 * .... - todo
 * .. - _generated
 * .... - ..
 */
public class BlogStructure {

    public static final String IMAGES_DIR_NAME = "images";

    private File root;

    public BlogStructure(File root) {
        this.root = root;
    }

    public File getArticlesDir() {
        return new File(root, "articles");
    }

    public File[] getArticles() {
        return getArticlesDir().listFiles();
    }


    public File getGenerateDir() {
        return new File(root, "_generated");
    }

    public File getImagesDir() {
        return new File(root, IMAGES_DIR_NAME);
    }

    public File getRoot() {
        return root;
    }
}

