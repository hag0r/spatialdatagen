name := "spatialdatagen"

scalaVersion := "2.11.8"

lazy val root = (project in file("."))

libraryDependencies ++= Seq(
   "org.scalatest" %% "scalatest" % "3.0.0" % "test" withSources(),
   "org.scalacheck" %% "scalacheck" % "1.13.2" % "test" ,
   "com.github.scopt" %% "scopt" % "3.5.0"
)

test in assembly := {}
logBuffered in Test := false

parallelExecution in Test := false

mainClass in (Compile, packageBin) := Some("dbis.datagen.spatial.Generator")

mainClass in (Compile, run) := Some("dbis.datagen.spatial.Generator")

mainClass in assembly := Some("dbis.datagen.spatial.Generator")

assemblyJarName in assembly := s"${name.value}.jar"

// assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
