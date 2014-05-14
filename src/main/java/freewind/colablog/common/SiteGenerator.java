package freewind.colablog.common;

import freewind.colablog.models.Article;
import freewind.colablog.structrue.BlogStructure;
import freewind.colablog.utils.IO;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import java.io.File;
import java.io.StringWriter;

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

            File targetFile = new File(blogStructure.getGenerateDir(), targetHtmlFileName(article));
            Template template = getTemplate("article.html");
            String html = render(template, article);
            IO.writeStringToFile(targetFile, html);
        }

        System.out.println("Generated " + articles.length + " files!");
    }

    private String render(Template template, Article article) {
        VelocityContext context = new VelocityContext();
        context.put("article", article);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    private Template getTemplate(String filename) {
        File templateFile = blogStructure.getTemplate(filename);
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
        velocityEngine.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
        velocityEngine.setProperty("file.resource.loader.path", templateFile.getParentFile().getAbsolutePath());
        velocityEngine.init();

        return velocityEngine.getTemplate(templateFile.getName());
    }


    private String targetHtmlFileName(Article article) {
        String fileName = article.getFileName();
        String basename = StringUtils.substringBeforeLast(fileName, ".");
        return basename + ".html";
    }

}
