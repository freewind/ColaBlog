package freewind.colablog

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{Priority, HBox}
import scalafx.scene.control.TextArea
import org.markdown4j.Markdown4jProcessor

object App extends JFXApp {

  val txtEditor = new TextArea {
    hgrow = Priority.ALWAYS
    text = "markdown here"
  }
  val txtPreview = new TextArea {
    hgrow = Priority.ALWAYS
    text <== txtEditor.text.map(markdown2html)
  }

  stage = {
    new JFXApp.PrimaryStage {
      title = "Cola Blog"
      width = 1200
      height = 600
      scene = new Scene {
        theScene =>
        content = new HBox {
          prefWidth <== theScene.width
          prefHeight <== theScene.height
          hgrow = Priority.ALWAYS
          content = List(
            txtEditor,
            txtPreview
          )
        }
      }
    }
  }

  private def markdown2html(md: String): String = new Markdown4jProcessor().process(md)
}
