package freewind.colablog

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, Priority, HBox}
import scalafx.scene.control.TextArea
import org.markdown4j.Markdown4jProcessor
import scalafx.scene.web.WebView
import javafx.beans.value.{ObservableValue, ChangeListener}


object App extends JFXApp {

  val WidthUnit = 1

  val editor = new TextArea {
    prefWidth = WidthUnit
    hgrow = Priority.ALWAYS
    text = """## markdown here ##
             |
             |> Hello ColaBlog!""".stripMargin
  }

  val preview = new WebView {
    prefWidth = WidthUnit
    hgrow = Priority.ALWAYS
    engine.loadContent(markdown2html(editor.text.value))

    editor.text.addListener(new ChangeListener[String] {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String) {
        engine.loadContent(markdown2html(newValue))
      }
    })
  }

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
