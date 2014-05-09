package freewind.colablog

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{Priority, HBox}
import scalafx.scene.control.TextArea
import org.markdown4j.Markdown4jProcessor
import scalafx.scene.web.WebView
import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.text.Font

object App extends JFXApp {

  val WidthUnit = 1

  val editor: TextArea = new TextArea {
    id = "editor"
    prefWidth = WidthUnit
    wrapText = true
    hgrow = Priority.ALWAYS

    this.onKeyPressed = KeyshortHandler {
      case Keyshorts.IncreaseFontSize =>
        val v: Double = font.value.getSize
        editor.setStyle("-fx-font-size: " + (v + 2) + "px;")
      case Keyshorts.DecreaseFontSize =>
        val v: Double = font.value.getSize
        editor.setStyle("-fx-font-size: " + (v - 2) + "px;")
    }
  }

  val preview: WebView = new WebView {
    prefWidth = WidthUnit
    hgrow = Priority.ALWAYS

    editor.text.addListener(new ChangeListener[String] {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String) {
        engine.loadContent(
          """
            |<script>
            |function resizeText(newFontSize) {
            |  document.body.style.fontSize = newFontSize + "px";
            |}
            |</script>
          """.stripMargin +
            "<div id='pbody'>" + markdown2html(newValue) + "</div>")
      }
    })
    editor.font.addListener(new ChangeListener[Font] {
      override def changed(observable: ObservableValue[_ <: Font], oldValue: Font, newValue: Font) {
        if (preview.getEngine.getDocument != null) {
          val newFontSize = newValue.getSize
          preview.getEngine.executeScript(s"resizeText($newFontSize)")
        }
      }
    })

  }

  editor.text = """## markdown here ##
                  |
                  |> Hello ColaBlog!""".stripMargin
  private val FontStyle = """
                            |    -fx-font-size: 29px;
                            |    -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);
                            |    -fx-stroke: black;
                            |    -fx-stroke-width: 1;
                          """.stripMargin

  editor.setStyle(FontStyle)

  val theScene: Scene = new Scene {
    theScene =>
    content = new HBox {
      prefWidth <== theScene.width
      prefHeight <== theScene.height
      hgrow = Priority.ALWAYS
      content = List(
        editor,
        preview
      )
    }
  }

  stage = {
    new JFXApp.PrimaryStage {
      title = "Cola Blog"
      width = 1200
      height = 600
      scene = theScene
    }
  }

  private def markdown2html(md: String): String = new Markdown4jProcessor().process(md)

}
