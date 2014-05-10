package freewind.colablog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.String.format;

public class AppController implements Initializable {

    private static final int INIT_FONT_SIZE = 26;

    @FXML
    private TextArea editor;
    @FXML
    private WebView preview;
    @FXML
    private ListView articlesPane;

    private final Keymap keymap = new Keymap();

    @FXML
    public void toggleArticlesPane(ActionEvent event) {
        articlesPane.setVisible(!articlesPane.isVisible());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAutoGrowControls();
        setHidableControls();
        initEditor();
    }

    private void setHidableControls() {
        articlesPane.managedProperty().bind(articlesPane.visibleProperty());
    }

    private void setAutoGrowControls() {
        HBox.setHgrow(preview, Priority.ALWAYS);
        HBox.setHgrow(editor, Priority.ALWAYS);
    }

    private void initEditor() {
        editor.setStyle("-fx-font-size: " + (INIT_FONT_SIZE + 2) + "px");
        livePreview();
        setKeyshortForFontSizeChanging();
        editor.textProperty().set("# title #");
    }

    private void setKeyshortForFontSizeChanging() {
        editor.setOnKeyPressed(keyEvent -> {
            KeyShort keyShort = keymap.findKeyShort(keyEvent);
            if (keyShort != null) {
                handleKeyshort(keyShort);
            }
        });
        editor.fontProperty().addListener((x, oldValue, newValue) -> {
            if (preview.getEngine().getDocument() != null) {
                String cmd = format("resizeText(%s)", newValue.getSize());
                preview.getEngine().executeScript(cmd);
            }
        });
    }

    private void handleKeyshort(KeyShort keyShort) {
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
        return "<script>\n" +
                "function resizeText(newFontSize) {\n" +
                "  document.body.style.fontSize = newFontSize + \"px\";\n" +
                "}\n" +
                "</script>\n" +
                body;
    }

    private String markdown2html(String markdown) throws IOException {
        return new Markdown4jProcessor().process(markdown);
    }
}
