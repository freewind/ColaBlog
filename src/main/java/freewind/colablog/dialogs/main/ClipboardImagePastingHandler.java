package freewind.colablog.dialogs.main;

import freewind.colablog.controls.ClipboardPastingHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ClipboardImagePastingHandler implements ClipboardPastingHandler {

    @Override
    public boolean handle(Clipboard clipboard) {
        if (clipboard.hasImage()) {
            try {
                saveImageToFile(clipboard);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void saveImageToFile(Clipboard clipboard) throws IOException {
        Image image = clipboard.getImage();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        File targetFile = File.createTempFile("colablog", ".png");
        ImageIO.write(bufferedImage, "png", targetFile);
        System.out.println("Saved image to file: " + targetFile);
    }

}
