name := "scaRLa"
version := "pre"
scalaVersion := "2.11.8"

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "org.scalatest" %% "scalatest" % "2.2.6",
  "com.typesafe.akka" %% "akka-actor" % "2.4.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.9")
