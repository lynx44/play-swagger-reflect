import sbt.Keys._

name := "play-swagger-reflect"

version := "0.1-SNAPSHOT"

scalaVersion in Global := "2.11.8"

organization in Global := "xyz.mattclifton"

resolvers in Global ++= Seq(
  Resolver.jcenterRepo,
  Resolver.sonatypeRepo("snapshots")
)

lazy val play_swagger_reflect = Project(id = "play-swagger-reflect", base = file("."))
  .settings(
    libraryDependencies ++= Seq(
      "xyz.mattclifton" %% "play-swagger" % "0.3.4-PLAY2.5-SNAPSHOT",
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
      "xyz.mattclifton" %% "play-swagger" % "0.3.4-PLAY2.5-SNAPSHOT",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.play" %% "play-json" % "2.5.+" % "provided",
      "org.specs2" %% "specs2-core" % "3.6.6" % "test",
      "com.typesafe.play" %% "play" % "2.5.+" % "provided",
      "xyz.mattclifton" %% "play-stringent" % "2.5.+" % "provided"
    )
  ) dependsOn(play_swagger_reflect)

licenses in Global := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

homepage  in Global := Some(url("https://github.com/lynx44/play-swagger-reflect"))

pomExtra in Global := (<scm>
  <url>git@github.com:lynx44/play-swagger-reflect.git</url>
  <connection>scm:git:git@github.com:lynx44/play-swagger-reflect.git</connection>
</scm>
  <developers>
    <developer>
      <id>lynx44</id>
      <name>Matt Clifton</name>
      <url>https://github.com/lynx44</url>
    </developer>
  </developers>)

credentials in Global ~= { c =>
  (Option(System.getenv().get("SONATYPE_USERNAME")), Option(System.getenv().get("SONATYPE_PASSWORD"))) match {
    case (Some(username), Some(password)) =>
      c :+ Credentials(
        "Sonatype Nexus Repository Manager",
        "oss.sonatype.org",
        username,
        password)
    case _ => c
  }
}

publishTo in Global <<= version { v => //add credentials to ~/.sbt/sonatype.sbt
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository in Global := { _ => false }