import sbt._

object Dependencies {
  val akkaVersion = "2.7.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  lazy val alpakkaCsv = "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "5.0.0"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.4.5"
  lazy val jimfs = "com.google.jimfs" % "jimfs" % "1.2"
}
