name := "yahoo-stocks"

version := "1.0"

scalaVersion := "2.12.2"

val specs2Ver = "3.8.9-20170417195349-7b7973e"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % specs2Ver % "test",
  "org.specs2" %% "specs2-mock" % specs2Ver % "test"

)

scalacOptions in Test ++= Seq("-Yrangepos")