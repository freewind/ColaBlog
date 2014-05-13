package freewind.colablog.dialogs.main;

import freewind.colablog.AppInfo;
import freewind.colablog.common.WordCounter;
import freewind.colablog.controls.Editor;
import freewind.colablog.keymap.KeyShort;
import freewind.colablog.keymap.Keymap;
import freewind.colablog.models.Article;
import freewind.colablog.spring.AutowireFXMLDialog;
import freewind.colablog.spring.SpringController;
import freewind.colablog.utils.IO;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.springframework.beans.factory.annotation.Autowired;

public class EditorController implements SpringController {

    @Autowired
    private AppInfo appInfo;
    @FXML
    private Editor editor;
    @FXML
    private BorderPane editorPane;
    @FXML
    private Label editorStatus;
    @Autowired
    private Keymap keymap;
    @Autowired
    private WordCounter wordCounter;

    private AutowireFXMLDialog autowireFXMLDialog;

    private Double initFontSize;
    private Article currentArticle;

    @Override
    public void postInit() {
        System.out.println("############ postInit!");
        uiSettings();
        keyshortForChangingFontSize();
        countWord();
        defaultCotnent();
    }

    private void defaultCotnent() {
        editor.textProperty().set("# title #");
    }

    private void countWord() {
        editorStatus.textProperty().bind(Bindings.createStringBinding(
                () -> String.valueOf(wordCounter.count(editor.getText())),
                editor.textProperty()
        ));
    }

    private void uiSettings() {
        HBox.setHgrow(editorPane, Priority.ALWAYS);
        HBox.setHgrow(editor, Priority.ALWAYS);
    }

    private void keyshortForChangingFontSize() {
        editor.setOnKeyPressed(keyEvent -> {
            System.out.println(keyEvent);
            KeyShort keyShort = keymap.findKeyShort(keyEvent);
            System.out.println("######### keyshort: " + keyShort);
            if (keyShort != null) {
                handleKeyshort(keyShort);
            }
        });
    }

    private void handleKeyshort(KeyShort keyShort) {
        double fontSize = editor.fontProperty().getValue().getSize();
        switch (keyShort) {
            case IncreaseFontSize:
                if (initFontSize == null) {
                    initFontSize = fontSize;
                }
                editor.setStyle("-fx-font-size: " + (fontSize + 2) + "px");
                return;
            case DecreaseFontSize:
                if (initFontSize == null) {
                    initFontSize = fontSize;
                }
                editor.setStyle("-fx-font-size: " + (fontSize - 2) + "px");
                return;
            case NormalFontSize:
                if (initFontSize != null) {
                    editor.setStyle("-fx-font-size: " + initFontSize + "px");
                }
                return;
            case Save:
                System.out.println("####### pressed Save");
                save();
                return;
        }
    }

    public void save() {
        if (currentArticle != null) {
            currentArticle.setContent(editor.getText());
            IO.writeStringToFile(currentArticle.getFile(), currentArticle.getFullContent());
            System.out.println("saved!");
        }
    }

    public Editor getEditor() {
        return editor;
    }

    public void loadArticle(Article currentArticle) {
        this.currentArticle = currentArticle;
        this.getEditor().textProperty().set(currentArticle.getContent());
        System.out.println("####### currentAritcle: " + currentArticle);
    }

}
