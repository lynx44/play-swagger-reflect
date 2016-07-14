name := "play-swagger-reflect"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.iheart" %% "play-swagger" % "0.3.4-PLAY2.5-SNAPSHOT",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.typesafe.play" %% "play-json" % "2.5.+" % "provided",
  "org.specs2" %% "specs2-core" % "3.6.6" % "test",
  "com.typesafe.play" %% "play" % "2.5.+" % "provided"
)