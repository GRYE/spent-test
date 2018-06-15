name := "spent-test"

version := "1.0"

scalacOptions += "-Ypartial-unification"

libraryDependencies := Seq(
  "org.typelevel" %% "cats-core" % "1.0.1",
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
)

scalaVersion := "2.12.6"
        