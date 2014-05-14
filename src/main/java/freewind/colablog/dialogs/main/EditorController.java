package freewind.colablog.dialogs.main;

import freewind.colablog.AppInfo;
import freewind.colablog.common.WordCounter;
import freewind.colablog.controls.ClipboardPastingHandler;
import freewind.colablog.controls.Editor;
import freewind.colablog.keymap.KeyShort;
import freewind.colablog.keymap.Keymap;
import freewind.colablog.models.Article;
import freewind.colablog.spring.AutowireFXMLDialog;
import freewind.colablog.spring.SpringController;
import freewind.colablog.structrue.BlogStructure;
import freewind.colablog.utils.IO;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        savePastingImage();
        countWord();
        defaultCotnent();
    }

    private void savePastingImage() {
        getEditor().setClipboardPastingHandler(new ClipboardImagePastingHandler());
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

    private class ClipboardImagePastingHandler implements ClipboardPastingHandler {

        @Override
        public boolean handle(Clipboard clipboard) {
            if (clipboard.hasImage()) {
                try {
                    File imageFile = saveImageToFile(clipboard);
                    String path = String.format("/%s/%s", BlogStructure.IMAGES_DIR_NAME, imageFile.getName());
                    String imageTag = "![" + imageFile.getName() + "](" + path + ")";

                    getEditor().insertText(getEditor().getAnchor(), imageTag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }

        private File saveImageToFile(Clipboard clipboard) throws IOException {
            Image image = clipboard.getImage();
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            File imagesDir = appInfo.getBlogStructure().getImagesDir();
            FileUtils.forceMkdir(imagesDir);
            File targetFile = new File(imagesDir, newImageFileName(imagesDir));
            ImageIO.write(bufferedImage, "png", targetFile);
            return targetFile;
        }

        private String newImageFileName(File imagesDir) {
            return String.format("%s_%s.png",
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                    imagesDir.list().length + 1);
        }
    }

}
