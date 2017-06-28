name := "time-series"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"     // 2.11 only
)

enablePlugins(AssemblyPlugin)