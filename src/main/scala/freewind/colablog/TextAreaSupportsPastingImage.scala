package freewind.colablog

import javafx.scene.control.TextArea
import scalafx.scene.input.Clipboard
import javax.imageio.ImageIO
import javafx.embed.swing.SwingFXUtils
import java.io.File

class TextAreaSupportsPastingImage extends TextArea {
  override def paste(): Unit = {
    val clipboard = Clipboard.systemClipboard
    if (clipboard.hasImage) {
      val image = clipboard.getImage
      val bufferedImage = SwingFXUtils.fromFXImage(image, null)
      val targetFile = File.createTempFile("colablog", ".png")
      ImageIO.write(bufferedImage, "png", targetFile)
      println("Saved image to file: " + targetFile)
    } else {
      super.paste()
    }
  }
}
