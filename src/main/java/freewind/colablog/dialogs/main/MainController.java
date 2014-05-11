package freewind.colablog.dialogs.main;

import freewind.colablog.AppInfo;
import freewind.colablog.common.NodeDumper;
import freewind.colablog.common.WordCounter;
import freewind.colablog.controls.Editor;
import freewind.colablog.keymap.KeyShort;
import freewind.colablog.keymap.Keymap;
import freewind.colablog.models.Article;
import freewind.colablog.spring.AutowireFXMLDialog;
import freewind.colablog.spring.PostInitController;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.String.format;

public class MainController implements PostInitController {

    @Autowired
    private AppInfo appInfo;
    @FXML
    private Editor editor;
    @FXML
    private WebView preview;
    @FXML
    private ListView<Article> articleListView;
    @FXML
    private HBox mainContainer;
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

    @FXML
    public void toggleArticlesPane() {
        articleListView.setVisible(!articleListView.isVisible());
    }

    @FXML
    public void dumpNodes() {
        new NodeDumper().dump(mainContainer);
    }

    @Override
    public void postInit() {
        setAutoGrowControls();
        setHidableControls();
        initEditor();
        loadArticleList();
        showArticleWhenClick();
    }

    private void showArticleWhenClick() {
        articleListView.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Article article = articleListView.getSelectionModel().getSelectedItem();
                try {
                    editor.loadArticle(article);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadArticleList() {
        File[] files = appInfo.getCurrentBlogDir().listFiles();
        if (files != null) {
            ObservableList<Article> items = articleListView.getItems();
            Arrays.asList(files).stream()
                    .map((file) -> new File(appInfo.getCurrentBlogDir(), file.getName()))
                    .map((file) -> new Article(file.getPath(), "xxx"))
                    .forEach(items::add);
        }
    }

    private void setHidableControls() {
        articleListView.managedProperty().bind(articleListView.visibleProperty());
    }

    private void setAutoGrowControls() {
        HBox.setHgrow(preview, Priority.ALWAYS);
        HBox.setHgrow(editorPane, Priority.ALWAYS);
        HBox.setHgrow(editor, Priority.ALWAYS);
    }

    private void initEditor() {
        editor.setPreview(preview);
        keyshortForChangingFontSize();
        editor.textProperty().set("# title #");
        editorStatus.textProperty().bind(Bindings.createStringBinding(
                () -> String.valueOf(wordCounter.count(editor.getText())),
                editor.textProperty()
        ));
    }

    private void keyshortForChangingFontSize() {
        editor.setOnKeyPressed(keyEvent -> {
            KeyShort keyShort = keymap.findKeyShort(keyEvent);
            if (keyShort != null) {
                try {
                    handleKeyshort(keyShort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        editor.fontProperty().addListener((x, oldValue, newValue) -> {
            if (preview.getEngine().getDocument() != null) {
                String cmd = format("resizeText(%s)", newValue.getSize());
                preview.getEngine().executeScript(cmd);
            }
        });
    }

    private void handleKeyshort(KeyShort keyShort) throws IOException {
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
                editor.save();
                return;
        }
    }

}
