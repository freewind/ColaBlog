package freewind.colablog.controls;

import freewind.colablog.ArticleItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Editor extends TextArea {

    private WebView preview;

    private ArticleItem currentArticle;

    public void loadArticle(ArticleItem article) throws IOException {
        this.currentArticle = article;
        this.textProperty().set(article.getContent());
    }

    public ArticleItem getCurrentArticle() {
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
        syncScroll();
    }

    private void syncScroll() {
        this.addEventFilter(ScrollEvent.ANY, (event) -> {
            ScrollBar editorBar = getVerticalScrollBar();
            preview.getEngine().executeScript("scrollToPercent(" + editorBar.getValue() + ")");
        });
    }

    private ScrollBar getVerticalScrollBar() {
        return (ScrollBar) this.lookup(".scroll-bar:vertical");
    }

}
