package freewind.colablog.dialogs.main;

import freewind.colablog.AppInfo;
import freewind.colablog.common.HtmlWrapper;
import freewind.colablog.common.NodeDumper;
import freewind.colablog.common.SiteGenerator;
import freewind.colablog.controls.Editor;
import freewind.colablog.keymap.Keymap;
import freewind.colablog.models.Article;
import freewind.colablog.spring.SpringController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.markdown4j.Markdown4jProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.String.format;

public class MainController implements SpringController {

    @Autowired
    private AppInfo appInfo;
    @FXML
    private BorderPane editorPane;
    @FXML
    private WebView preview;
    @FXML
    private ListView<Article> articleListView;
    @FXML
    private HBox mainContainer;
    @Autowired
    private Keymap keymap;
    @FXML
    private EditorController editorPaneController;
    @FXML
    private ListView<String> fontListView;

    @FXML
    public void toggleArticlesPane() {
        articleListView.setVisible(!articleListView.isVisible());
    }

    @FXML
    public void toggleFontPane() {
        fontListView.setVisible(!fontListView.isVisible());
    }

    @FXML
    public void dumpNodes() {
        new NodeDumper().dump(mainContainer);
    }

    @Override
    public void postInit() {
        uiSettings();
        editorFontChangeTriggersPreview();
        loadArticleList();
        loadAvailableFonts();
        showArticleWhenClick();
        livePreview();
        syncScroll();
    }

    private void loadAvailableFonts() {
        fontListView.getItems().addAll(Font.getFamilies());
        fontListView.setOnMouseClicked((event) -> {
            String fontName = fontListView.getSelectionModel().getSelectedItem();
            System.out.println(fontName);
            getEditor().setStyle("-fx-font-family: '" + fontName + "'");
        });
    }

    private void uiSettings() {
        HBox.setHgrow(preview, Priority.ALWAYS);
        HBox.setHgrow(editorPane, Priority.ALWAYS);
        articleListView.managedProperty().bind(articleListView.visibleProperty());
        fontListView.managedProperty().bind(fontListView.visibleProperty());
    }

    private void syncScroll() {
        final MutableBoolean haveSetScrollHandler = new MutableBoolean(false);
        getEditor().textProperty().addListener((event) -> {
            ScrollBar scrollBar = getEditor().getVerticalScrollBar();
            if (scrollBar != null) {
                if (haveSetScrollHandler.isFalse()) {
                    scrollBar.valueProperty().addListener((e2) -> {
                        preview.getEngine().executeScript("scrollToPercent(" + scrollBar.getValue() + ")");
                    });
                    haveSetScrollHandler.setValue(true);
                }
            }
        });
    }

    private void livePreview() {
        getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            String body = markdown2html(newValue);
            String html = new HtmlWrapper().full(body);
            System.out.println(html);
            preview.getEngine().loadContent(html);
        });
    }

    private String markdown2html(String markdown) {
        try {
            return new Markdown4jProcessor().process(markdown);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showArticleWhenClick() {
        articleListView.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                editorPaneController.loadArticle(articleListView.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void loadArticleList() {
        File[] files = appInfo.getBlogStructure().getArticles();
        if (files != null) {
            ObservableList<Article> items = articleListView.getItems();
            Arrays.asList(files).stream()
                    .map(Article::new)
                    .forEach(items::add);
        }
    }

    private void editorFontChangeTriggersPreview() {
        getEditor().fontProperty().addListener((x, oldValue, newValue) -> {
            if (preview.getEngine().getDocument() != null) {
                String cmd = format("resizeText(%s)", newValue.getSize());
                preview.getEngine().executeScript(cmd);
            }
        });
    }

    private Editor getEditor() {
        return editorPaneController.getEditor();
    }

    @FXML
    public void generateSite() {
        new SiteGenerator(appInfo.getBlogStructure()).generate();
    }

}
