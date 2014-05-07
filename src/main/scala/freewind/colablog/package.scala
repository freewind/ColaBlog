package freewind

import scalafx.beans.property.StringProperty
import javafx.beans.binding.Bindings
import java.util.concurrent.Callable
import scalafx.beans.binding.StringBinding
import scalafx.Includes._

package object colablog {

  implicit class StringPropertyEnhancer(strProperty: StringProperty) {
    def map(converter: String => String): StringBinding = {
      Bindings.createStringBinding(new Callable[String] {
        override def call(): String = converter(strProperty.value)
      }, strProperty)
    }
  }

}
