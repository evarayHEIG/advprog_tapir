ThisBuild / scalaVersion := "3.8.0"
ThisBuild / organization := "ch.epfl.scala"
ThisBuild / fork := true

lazy val tapirVersion  = "1.13.18"
lazy val http4sVersion = "0.23.30"
lazy val catsVersion   = "3.5.7"
lazy val sttpVersion   = "3.10.3"

// dépendances communes aux 3 modules
lazy val commonDeps = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core"        % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe"  % tapirVersion,
  "io.circe"                    %% "circe-generic"      % "0.14.10",
  "org.typelevel"               %% "cats-effect"        % catsVersion,
)

lazy val shared = project
  .in(file("shared"))
  .settings(
    name := "shared",
    libraryDependencies ++= commonDeps
  )

lazy val backend = project
  .in(file("backend"))
  .dependsOn(shared) 
  .settings(
    name := "backend",
    fork := true,
    libraryDependencies ++= commonDeps ++ Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"     % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "org.http4s"                  %% "http4s-ember-server"      % http4sVersion,
      "ch.qos.logback"               % "logback-classic"          % "1.5.18" % Runtime,
    )
  )

lazy val client = project
  .in(file("client"))
  .dependsOn(shared)
  .settings(
    name := "client",
    fork := true,
    libraryDependencies ++= commonDeps ++ Seq(
      "com.softwaremill.sttp.tapir"   %% "tapir-sttp-client" % tapirVersion,
      "com.softwaremill.sttp.client3" %% "cats"              % sttpVersion,
    )
  )

lazy val root = project
  .in(file("."))
  .aggregate(shared, backend, client)