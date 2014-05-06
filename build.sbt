name := "ColaBlog"

version := "0.1.0"

scalaVersion := "2.11.0"

libraryDependencies ++= List(
  "org.scalafx" % "scalafx_2.11" % "8.0.0-R4"
)

initialize := {
  val _ = initialize.value // run the previous initialization
  val classVersion = sys.props("java.class.version")
  val specVersion = sys.props("java.specification.version")
  if (specVersion.toDouble < 1.8) {
    throw new Exception("Jdk 1.8+ required")
  }
}