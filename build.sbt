import Dependencies._
import sbtassembly.AssemblyPlugin.defaultShellScript

ThisBuild / scalaVersion     := "2.13.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"
// sbt-assembly
ThisBuild / assemblyPrependShellScript := Some(defaultShellScript)

lazy val root = (project in file("."))
  .settings(
    name := s"scleanshot",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.9" % "test"
    ),
    // sbt-assembly
    assembly / assemblyJarName := "scleanshot.jar",
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
