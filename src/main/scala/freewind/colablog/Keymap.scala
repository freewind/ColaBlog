package freewind.colablog

import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent}

import KeyCode._
import Keyshorts._

case class KeyshortHandler(action: PartialFunction[Keyshort, Unit]) extends EventHandler[KeyEvent] {
  val fastKeypressFix = new FastKeypressFix

  override def handle(event: KeyEvent): Unit = fastKeypressFix.fix {
    val foundKeyshort = Keymap.mapping.find({
      case (keycodes, keyshort) =>
        keycodes.forall {
          case META => event.isMetaDown
          case SHIFT => event.isShiftDown
          case ALT => event.isAltDown
          case normalCode => event.getCode == normalCode
        }
    }).map(_._2)

    foundKeyshort.foreach {
      keyshort => if (action.isDefinedAt(keyshort)) {
        action.apply(keyshort)
      }
    }
  }

}

// try to resolve a javafx bug
// http://stackoverflow.com/questions/23543282/why-press-command-on-mac-os-will-trigger-4-key-events
class FastKeypressFix {
  var lastKeyshortTime: Long = 0

  def fix(f: => Any) {
    val now = System.currentTimeMillis
    if (now - lastKeyshortTime > 20) {
      f
      lastKeyshortTime = now
    }
  }
}

object Keymap {
  val mapping = Map(
    Seq(META, EQUALS) -> IncreaseFontSize,
    Seq(META, MINUS) -> DecreaseFontSize,
    Seq(META, DIGIT0) -> NormalFontSize,
    Seq(META, F) -> SearchInEditor,
    Seq(META, V) -> Paste
  )
}

object Keyshorts {

  trait Keyshort

  object IncreaseFontSize extends Keyshort

  object DecreaseFontSize extends Keyshort

  object NormalFontSize extends Keyshort

  object SearchInEditor extends Keyshort

  object Paste extends Keyshort

}