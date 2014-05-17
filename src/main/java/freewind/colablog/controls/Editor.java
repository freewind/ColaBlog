package freewind.colablog.controls;

import com.google.common.collect.Lists;
import freewind.colablog.common.TextOperations;
import freewind.colablog.keymap.KeyShort;
import freewind.colablog.keymap.Keymap;
import freewind.colablog.models.Article;
import freewind.colablog.models.Articles;
import freewind.colablog.spring.SpringInjectable;
import freewind.colablog.structrue.BlogStructure;
import freewind.colablog.utils.IO;
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

    private Article currentArticle;

    private ClipboardPastingHandler clipboardPastingHandler;

    private BlogStructure blogStructure;
    private Articles articles;
    private TextOperations textOperations;
    @Autowired
    private Keymap keymap;
    private TextOperations.MoveUpDownHandler upDownHandler;

    public void setClipboardPastingHandler(ClipboardPastingHandler clipboardPastingHandler) {
        this.clipboardPastingHandler = clipboardPastingHandler;
    }

    private void setKeyshortHanlers() {
        List<KeyshortHandler> handlers = Lists.newArrayList(
                new FontSizeKeyshortHandler(this),
                new TabKeyshortHandler(this)
        );

        this.setOnKeyPressed(keyEvent -> {
            KeyShort keyShort = keymap.findKeyShort(keyEvent);
            if (keyShort != null) {
                for (KeyshortHandler handler : handlers) {
                    if (handler.handle(keyShort, keyEvent)) {
                        break;
                    }
                }
                handleOther(keyShort, keyEvent);
            }
        });
    }

    private int previousUpDownCaretPosition = -1;

    private void handleOther(KeyShort keyShort, KeyEvent keyEvent) {
        switch (keyShort) {
            case ArrowUp:
                keyEvent.consume();
                if (upDownHandler == null || getCaretPosition() != previousUpDownCaretPosition) {
                    upDownHandler = textOperations.createUpDownHandler(getCaretPosition());
                }
                int pos = upDownHandler.moveUp();
                positionCaret(pos);
                previousUpDownCaretPosition = pos;
                System.out.println("Move up");
                return;
            case ArrowDown:
                keyEvent.consume();
                if (upDownHandler == null || getCaretPosition() != previousUpDownCaretPosition) {
                    upDownHandler = textOperations.createUpDownHandler(getCaretPosition());
                }
                int pos1 = upDownHandler.moveDown();
                positionCaret(pos1);
                previousUpDownCaretPosition = pos1;
                System.out.println("Move down");
                return;
            case Save:
                this.save();
                return;
        }
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
        textOperations = new TextOperations(this.getText());
        this.textProperty().addListener((event) -> {
            textOperations = new TextOperations(getText());
            upDownHandler = null;
        });
        this.caretPositionProperty().addListener((event) -> {
            previousUpDownCaretPosition = -1;
        });

    }

}
