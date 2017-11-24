name := "codemotion17"

scalaVersion := "2.12.3"

organization := "org.hablapps"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1"
)

enablePlugins(TutPlugin)

tutTargetDirectory := baseDirectory.value

scalacOptions ++= Seq(
  "-Xlint",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:higherKinds")

