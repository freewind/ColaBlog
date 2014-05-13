package freewind.colablog.common;

import freewind.colablog.models.Article;
import freewind.colablog.structrue.BlogStructure;
import freewind.colablog.utils.IO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

public class SiteGenerator {

    private BlogStructure blogStructure;

    public SiteGenerator(BlogStructure blogStructure) {
        this.blogStructure = blogStructure;
    }

    public void generate() {
        File targetDir = blogStructure.getGenerateDir();
        IO.cleanDir(targetDir);

        File[] articles = blogStructure.getArticles();
        for (File file : articles) {
            Article article = new Article(file);
            File targetFile = new File(targetDir, targetHtmlFileName(article));
            String html = new MarkdownConverter().toHtml(article.getContent());
            IO.writeStringToFile(targetFile, html);
        }
        System.out.println("Generated " + articles.length + " files!");
    }


    private String targetHtmlFileName(Article article) {
        String fileName = article.getFileName();
        String basename = StringUtils.substringBeforeLast(fileName, ".");
        return basename + ".html";
    }

}
