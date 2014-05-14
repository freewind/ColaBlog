package freewind.colablog.controls;

import freewind.colablog.models.Article;
import freewind.colablog.models.Articles;
import freewind.colablog.structrue.BlogStructure;
import freewind.colablog.utils.IO;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Editor extends TextArea {

    private Article currentArticle;

    private ClipboardPastingHandler clipboardPastingHandler;

    private BlogStructure blogStructure;
    private Articles articles;

    public void setClipboardPastingHandler(ClipboardPastingHandler clipboardPastingHandler) {
        this.clipboardPastingHandler = clipboardPastingHandler;
    }

    @Override
    public void paste() {
        System.out.println("### paste ###");
        if (clipboardPastingHandler.handle(Clipboard.getSystemClipboard())) {
            return;
        }
        super.paste();
    }

    public ScrollBar getVerticalScrollBar() {
        return (ScrollBar) this.lookup(".scroll-bar:vertical");
    }

    public void save() {
        if (currentArticle != null) {
            currentArticle.setContent(this.getText());
            IO.writeStringToFile(currentArticle.getFile(), currentArticle.getFullContent());
            System.out.println("saved!");
        } else {
            String filename = inputArticleName();
            if (canUse(filename)) {
                File file = saveToFile(filename);
                currentArticle = new Article(file);
                articles.add(currentArticle);
                save();
            } else {
                Dialogs.create().message("File exists, please choose another name").showError();
            }
        }
    }

    private boolean canUse(String filename) {
        File file = new File(blogStructure.getArticlesDir(), filename);
        return !file.exists();
    }

    private File saveToFile(String filename) {
        File file = new File(blogStructure.getArticlesDir(), filename);
        IO.writeStringToFile(file, getText());
        return file;
    }

    private String inputArticleName() {
        String prefix = new SimpleDateFormat("yyyy-MM-dd-").format(new Date());
        return Dialogs.create()
                .owner(null)
                .title("Save article")
                .masthead("Use English words and '-' only")
                .message("File name")
                .showTextInput(prefix + "my-blog.md");
    }


    public void loadArticle(Article currentArticle) {
        this.currentArticle = currentArticle;
        this.textProperty().set(currentArticle.getContent());
        System.out.println("####### currentAritcle: " + currentArticle);
    }

    public void newArticle() {
        this.currentArticle = null;
        this.setText("");
    }

    public void setBlogStructure(BlogStructure blogStructure) {
        this.blogStructure = blogStructure;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }
}
