package freewind.colablog

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{Priority, HBox}
import scalafx.scene.control.TextArea

object App extends JFXApp {

  stage = new JFXApp.PrimaryStage {
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
          new TextArea {
            hgrow = Priority.ALWAYS
            text = "markdown here"
          },
          new TextArea {
            hgrow = Priority.ALWAYS
            text = "rendered html here"
          }
        )
      }
    }
  }

}
