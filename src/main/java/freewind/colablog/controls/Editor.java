package freewind.colablog.controls;

import freewind.colablog.common.HtmlWrapper;
import freewind.colablog.models.Article;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import org.markdown4j.Markdown4jProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Editor extends TextArea {

    private WebView preview;

    private Article currentArticle;

    public void loadArticle(Article article) throws IOException {
        this.currentArticle = article;
        this.textProperty().set(article.getContent());
    }

    public Article getCurrentArticle() {
        return currentArticle;
    }

    @Override
    public void paste() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasImage()) {
            try {
                saveImageToFile(clipboard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.paste();
        }
    }

    private void saveImageToFile(Clipboard clipboard) throws IOException {
        Image image = clipboard.getImage();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        File targetFile = File.createTempFile("colablog", ".png");
        ImageIO.write(bufferedImage, "png", targetFile);
        System.out.println("Saved image to file: " + targetFile);
    }

    public void setPreview(WebView preview) {
        this.preview = preview;
        livePreview();
        syncScroll();
    }

    private void livePreview() {
        this.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                String body = markdown2html(newValue);
                preview.getEngine().loadContent(new HtmlWrapper().full(body));
            } catch (IOException e) {
                // should never happen
                e.printStackTrace();
            }
        });
    }

    public void save() throws IOException {
        if (currentArticle != null) {
            FileUtils.writeStringToFile(currentArticle.getFile(), getText(), "UTF-8");
        }
    }

    class UserData {
        public boolean haveSetScrollHandler = false;
    }

    private void syncScroll() {
        this.textProperty().addListener((event) -> {
            ScrollBar scrollBar = getVerticalScrollBar();
            if (scrollBar != null) {
                UserData userData = (UserData) scrollBar.getUserData();
                if (userData == null) {
                    userData = new UserData();
                    scrollBar.setUserData(userData);
                }
                if (!userData.haveSetScrollHandler) {
                    scrollBar.valueProperty().addListener((e2) -> {
                        preview.getEngine().executeScript("scrollToPercent(" + scrollBar.getValue() + ")");
                    });
                    userData.haveSetScrollHandler = true;
                }
            }
        });
    }

    private ScrollBar getVerticalScrollBar() {
        return (ScrollBar) this.lookup(".scroll-bar:vertical");
    }

    private String markdown2html(String markdown) throws IOException {
        return new Markdown4jProcessor().process(markdown);
    }
}
