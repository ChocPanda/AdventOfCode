// *****************************************************************************
// Projects
// *****************************************************************************

lazy val AdventOfCode =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.scalaCheck % Test,
        library.utest      % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaCheck = "1.14.0"
      val utest      = "0.6.6"
    }
    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val utest      = "com.lihaoyi"    %% "utest"      % Version.utest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
commonSettings ++
fmtSettings ++
fixSettings ++
styleSettings

lazy val commonSettings =
  Seq(
    scalaVersion := "2.12.7",
    organization := "com.github.chocpanda",
    homepage := Option(url("https://github.com/ChocPanda/AdventOfCode")),
    startYear := Some(2018),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "ChocPanda",
        "Matt Searle",
        "mattsearle@ymail.com",
        url("https://github.com/ChocPanda/")
      )
    ),
    updateOptions := updateOptions.value.withGigahorse(false),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8",
      "-Ypartial-unification",
      "-Ywarn-unused-import"
    ),
    Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
    Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    Compile / compile / wartremoverWarnings ++= Warts.unsafe
  )

lazy val fmtSettings =
  Seq(
    scalafmtOnCompile := true
  )

lazy val fixSettings =
  Seq(
    libraryDependencies += compilerPlugin(scalafixSemanticdb),
    scalacOptions ++= Seq(
      "-Yrangepos",
      "-Ywarn-unused-import"
    )
  )

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
lazy val styleSettings = {
  Seq(
    scalastyleFailOnError := true,
    scalastyleFailOnWarning := true
  )
}

// *****************************************************************************
// Commands
// *****************************************************************************

addCommandAlias("fix", "; compile:scalafix; test:scalafix")
addCommandAlias("fixcheck", "; compile:scalafix --check; test:scalafix --check")
addCommandAlias("fmt", "; compile:scalafmt; test:scalafmt; scalafmtSbt")
addCommandAlias("fmtcheck", "; compile:scalafmtCheck; test:scalafmtCheck; scalafmtSbtCheck")
addCommandAlias("stylecheck", "; compile:scalastyle; test:scalastyle")
