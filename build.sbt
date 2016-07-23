name := "play-swagger-reflect"

version := "1.0"

scalaVersion in Global := "2.11.8"

organization in Global := "xyz.mattclifton"

resolvers in Global ++= Seq(
  Resolver.jcenterRepo,
  Resolver.sonatypeRepo("snapshots")
)

lazy val play_swagger_reflect = Project(id = "play-swagger-reflect", base = file("."))
  .settings(
    libraryDependencies ++= Seq(
      "com.iheart" %% "play-swagger" % "0.3.4-PLAY2.5-SNAPSHOT",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.play" %% "play-json" % "2.5.+" % "provided",
      "org.specs2" %% "specs2-core" % "3.6.6" % "test",
      "com.typesafe.play" %% "play" % "2.5.+" % "provided"
    )
  )

lazy val play_swagger_stringent = Project(id = "play-swagger-stringent", base = file("play-swagger-stringent"))
  .settings(
    name := "play-swagger-stringent",
    libraryDependencies ++= Seq(
      "com.iheart" %% "play-swagger" % "0.3.4-PLAY2.5-SNAPSHOT",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.play" %% "play-json" % "2.5.+" % "provided",
      "org.specs2" %% "specs2-core" % "3.6.6" % "test",
      "com.typesafe.play" %% "play" % "2.5.+" % "provided",
      "xyz.mattclifton" %% "play-stringent" % "2.5.+" % "provided"
    )
  ) dependsOn(play_swagger_reflect)