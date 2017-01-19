name := """play-react-prototype"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.pac4j" % "play-pac4j" % "2.6.1",
  "org.pac4j" % "pac4j-oidc" % "1.9.5" exclude("xml-apis" , "xml-apis"),
  "org.pac4j" % "pac4j-oauth" % "1.9.5",
  "org.pac4j" % "pac4j-jwt" % "1.9.5" exclude("commons-io" , "commons-io"),
  "commons-io" % "commons-io" % "2.4",
  "be.objectify" %% "deadbolt-java" % "2.5.1"
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

PlayKeys.playRunHooks <+= baseDirectory.map(Webpack.apply)

routesGenerator := InjectedRoutesGenerator

excludeFilter in (Assets, JshintKeys.jshint) := "*.js"

watchSources ~= { (ws: Seq[File]) =>
  ws filterNot { path =>
    path.getName.endsWith(".js") || path.getName == ("build")
  }
}

pipelineStages := Seq(digest, gzip)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


