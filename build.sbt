organization  := "bbc.linkeddata.storyline"

name          := "Banana RDF Storyline"

version       := "0.1-SNAPSHOT"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "joda-time" %  "joda-time"       % "2.3",
  "org.w3"    %% "banana-rdf"      % "0.4",
  "org.w3"    %% "banana-jena"     % "0.4",
  "org.w3"    %% "banana-sesame"   % "0.4"
)
