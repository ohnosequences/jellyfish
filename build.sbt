Nice.scalaProject

name          := "jellyfish-api"
organization  := "ohnosequences"
description   := "A Scala typesafe API for Jellyfish"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "ohnosequences"         %% "cosas"        % "0.8.0",
  "com.github.pathikrit"  %% "better-files" % "2.13.0",
  // "com.github.tototoshi"  %% "scala-csv"    % "1.2.2" % Test,
  "org.scalatest"         %% "scalatest"    % "2.2.5" % Test
)

bucketSuffix  := "era7.com"
