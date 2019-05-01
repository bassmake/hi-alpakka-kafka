ThisBuild / name := "hi-alpakka-kafka"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.12.8"

lazy val `hi-alpakka-kafka` = (project in file("."))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream-kafka" % "1.0.1",
      "ch.qos.logback" % "logback-classic" % "1.2.3",

      "com.typesafe.akka" %% "akka-stream-kafka-testkit" % "1.0.1" % Test,
      "io.github.embeddedkafka" %% "embedded-kafka-streams" % "2.2.0" % Test,
      "io.github.embeddedkafka" %% "embedded-kafka" % "2.2.0" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    )
  )

lazy val commonSettings = smlBuildSettings ++ Seq(
  // your settings, which can override some of smlBuildSettings
)