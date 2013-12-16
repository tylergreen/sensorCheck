name := "sensor check"

version := "0.1.0"

scalaVersion := "2.10.3"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots")


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.scalaz" %% "scalaz-core" % "7.0.5",
  "org.scalaz" %% "scalaz-concurrent" % "7.0.5",
//  "org.scalaz.stream" %% "scalaz-stream" % "0.2-SNAPSHOT",
  "org.apache.commons" % "commons-math3" % "3.2")
