package freewind.colablog.controls;

import freewind.colablog.keymap.KeyShort;
import freewind.colablog.keymap.Keymap;
import freewind.colablog.models.Article;
import freewind.colablog.models.Articles;
import freewind.colablog.spring.SpringInjectable;
import freewind.colablog.structrue.BlogStructure;
import freewind.colablog.utils.IO;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyEvent;
import org.controlsfx.dialog.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Editor extends TextArea implements SpringInjectable {

    public static final String TAB_SPACES = "    ";
    private Article currentArticle;

    private ClipboardPastingHandler clipboardPastingHandler;

    private BlogStructure blogStructure;
    private Articles articles;
    private Double initFontSize;

    @Autowired
    private Keymap keymap;

    public void setClipboardPastingHandler(ClipboardPastingHandler clipboardPastingHandler) {
        this.clipboardPastingHandler = clipboardPastingHandler;
    }

    private void setKeyshortHanlers() {
        this.setOnKeyPressed(keyEvent -> {
            KeyShort keyShort = keymap.findKeyShort(keyEvent);
            if (keyShort != null) {
                handleKeyshort(keyShort, keyEvent);
            }
        });
    }

    private void handleKeyshort(KeyShort keyShort, KeyEvent keyEvent) {
        double fontSize = this.fontProperty().getValue().getSize();
        switch (keyShort) {
            case IncreaseFontSize:
                if (initFontSize == null) {
                    initFontSize = fontSize;
                }
                this.setStyle("-fx-font-size: " + (fontSize + 2) + "px");
                return;
            case DecreaseFontSize:
                if (initFontSize == null) {
                    initFontSize = fontSize;
                }
                this.setStyle("-fx-font-size: " + (fontSize - 2) + "px");
                return;
            case NormalFontSize:
                if (initFontSize != null) {
                    this.setStyle("-fx-font-size: " + initFontSize + "px");
                }
                return;
            case Save:
                this.save();
                return;
            case Tab:
                keyEvent.consume();
                if (getSelection().getLength() == 0) {
                    this.insertText(getAnchor(), TAB_SPACES);
                } else {
                    List<Integer> lineSeparatorPositions = new TabPositionFinder(getText(), getSelection().getStart(), getSelection().getEnd()).find();
                    System.out.println("### positions: " + lineSeparatorPositions);
                    for (int position : lineSeparatorPositions) {
                        insertText(position, TAB_SPACES);
                    }
                }
                return;
            case ShiftTab:
                keyEvent.consume();
                List<Integer> lineSeparatorPositions = new TabPositionFinder(getText(), getSelection().getStart(), getSelection().getEnd()).find();
                System.out.println(lineSeparatorPositions);
                for (int position : lineSeparatorPositions) {
                    int end = findDeletionEnd(position);
                    if (end > position) {
                        deleteText(position, end);
                    }
                }
        }
    }

    private int findDeletionEnd(int position) {
        int end = position;
        for (int i = position; i < Math.min(getText().length(), position + TAB_SPACES.length()); i++) {
            if (getText().charAt(i) == ' ') {
                end = i + 1;
            } else {
                break;
            }
        }
        return end;
    }

    @Override
    public void paste() {
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

    @Override
    public void postInit() {
        setKeyshortHanlers();
    }

}
