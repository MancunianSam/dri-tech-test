import Dependencies._

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "uk.gov.nationalarchives"

lazy val root = (project in file("."))
  .settings(
    name := "dri-tech-lead-test",
    libraryDependencies ++= Seq(
      akkaStream,
      alpakkaCsv,
      scalaLogging,
      logback,
      jimfs % Test,
      scalaTest % Test
    )
  )
