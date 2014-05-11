package freewind.colablog;

import freewind.colablog.controls.Editor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import org.markdown4j.Markdown4jProcessor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.String.format;

public class AppController implements Initializable {

    private static final String BLOG_DIR = "/Users/freewind/blog";

    private static final int INIT_FONT_SIZE = 26;
    @FXML
    private TextArea myStyle;
    @FXML
    private Editor editor;
    @FXML
    private WebView preview;
    @FXML
    private ListView<ArticleItem> articleListView;
    @FXML
    private HBox mainContainer;

    private final Keymap keymap = new Keymap();

    @FXML
    public void toggleArticlesPane() {
        articleListView.setVisible(!articleListView.isVisible());
    }

    @FXML
    public void applyStyle() {
        mainContainer.setStyle(myStyle.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAutoGrowControls();
        setHidableControls();
        initEditor();
        initArticlesList();
    }

    private void initArticlesList() {
        ObservableList<ArticleItem> items = articleListView.getItems();
        File[] files = new File(BLOG_DIR).listFiles();
        if (files != null) {
            for (File file : files) {
                String path = new File(BLOG_DIR, file.getName()).getPath();
                items.add(new ArticleItem(path, "xxx"));
            }
        }

        articleListView.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                ArticleItem article = articleListView.getSelectionModel().getSelectedItem();
                System.out.println(article);
                try {
                    editor.loadArticle(article);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setHidableControls() {
        articleListView.managedProperty().bind(articleListView.visibleProperty());
    }

    private void setAutoGrowControls() {
        HBox.setHgrow(preview, Priority.ALWAYS);
        HBox.setHgrow(editor, Priority.ALWAYS);
    }

    private void initEditor() {
        editor.setPreview(preview);
        livePreview();
        keyshortForChangingFontSize();
        editor.textProperty().set("# title #");
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
        System.out.println("fontSize: " + fontSize);
        switch (keyShort) {
            case IncreaseFontSize:
                editor.setStyle("-fx-font-size: " + (fontSize + 2) + "px");
                return;
            case DecreaseFontSize:
                editor.setStyle("-fx-font-size: " + (fontSize - 2) + "px");
                return;
            case NormalFontSize:
                editor.setStyle("-fx-font-size: " + (INIT_FONT_SIZE - 2) + "px");
                return;
            case Save:
                editor.save();
                return;
        }
    }

    private void livePreview() {
        editor.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                String body = markdown2html(newValue);
                preview.getEngine().loadContent(fullHtml(body));
            } catch (IOException e) {
                // should never happen
                e.printStackTrace();
            }
        });
    }

    private String fullHtml(String body) {
        return "<style>\n" +
                "body,div,code {\n" +
                "  font-family: 'Hiragino Sans GB';\n" +
                "}\n" +
                "code {" +
                "  border: 1px solid #eee;" +
                "  background: #eee;" +
//                "  padding: 3px 8px;" +
                "}" +
                "pre {" +
                "  font-family: 'Hiragino Sans GB';" +
                "  background: #eee;" +
                "  padding: 5px 10px;" +
                "}\n" +
                "pre code {" +
                "  border: 0px" +
                "  background: #eee;" +
                "  padding: 0px;" +
                "}\n" +
                "blockquote {" +
                "  border-left: 8px solid #eee;" +
                "  background: #f6f6f6;" +
                "  color: #000;" +
                "  margin: 0px;" +
                "  padding: 2px 10px" +
                "}\n" +
                "</style>" +
                "<script>\n" +
                "function resizeText(newFontSize) {\n" +
                "  document.body.style.fontSize = newFontSize + \"px\";\n" +
                "}\n" +
                "function getDocHeight() {\n" +
                "    return Math.max(\n" +
                "        document.body.scrollHeight || 0, \n" +
                "        document.documentElement.scrollHeight || 0,\n" +
                "        document.body.offsetHeight || 0, \n" +
                "        document.documentElement.offsetHeight || 0,\n" +
                "        document.body.clientHeight || 0, \n" +
                "        document.documentElement.clientHeight || 0,\n" +
                "        0" +
                "    );\n" +
                "}\n" +
                "function scrollToPercent(p) {\n" +
                "    window.scrollTo(0,  p * getDocHeight());\n" +
                "}\n" +
                "</script>\n" +
                body;
    }

    private String markdown2html(String markdown) throws IOException {
        return new Markdown4jProcessor().process(markdown);
    }
}
