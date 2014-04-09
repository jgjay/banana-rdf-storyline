import org.w3.banana._
import org.w3.banana.diesel._
import org.w3.banana.jena._
import org.w3.banana.sesame._
import org.joda.time.DateTime
import java.util.UUID

object Runner extends BananaRDFStoryline[Jena, Turtle] with App {}

abstract class BananaRDFStoryline[Rdf <: RDF, Format]()(implicit ops: RDFOps[Rdf], writer: RDFWriter[Rdf, Format], reader: RDFReader[Rdf, Format]) {
  import ops._
  
  val rdf  = RDFPrefix[Rdf]
  val nsl  = StorylinePrefix[Rdf]
  val core = CoreConceptsPrefix[Rdf]
  
  val graph =
    (
      ThingURI(UUID.randomUUID)
        -- rdf.typ ->- nsl.Storyline
        -- core.preferredLabel ->- "Disappearence of Malaysia Airlines Flight 370"
        -- core.shortLabel ->- "Malaysia Airlines flight disappearance"
        -- core.disambiguationHint ->- "March 2014"
        -- nsl.synopsis ->- "Malaysia Airlines Flight 370 was a scheduled international passenger flight that disappeared on 8 March 2014."
        -- nsl.dateCreated ->- new DateTime()
    ).graph
    
  val turtle = writer.asString(graph, "").get
  
  val parsedGraph = reader.read(turtle, "").get.getAllInstancesOf(nsl.Storyline)
  
  val preferredLabel      = (parsedGraph / core.preferredLabel).as[String].get
  val shortLabel          = (parsedGraph / core.shortLabel).as[String].get
  val disambiguationHint  = (parsedGraph / core.disambiguationHint).as[String].get
  val synopsis            = (parsedGraph / nsl.synopsis).as[String].get
  val dateCreated         = (parsedGraph / nsl.dateCreated).as[DateTime].get
  
  println(s"Preferred Label: $preferredLabel")
  println(s"Short Label: $shortLabel")
  println(s"Disambiguation: $disambiguationHint")
  println(s"Synopsis: $synopsis")
  println(s"Date Created: $dateCreated")
}

object ThingURI {
  def apply[Rdf <: RDF](guid: UUID)(implicit ops: RDFOps[Rdf]) = ops.URI(s"http://www.bbc.co.uk/things/$guid#id")
}

object StorylinePrefix {
  def apply[Rdf <: RDF](implicit ops: RDFOps[Rdf]) = new StorylinePrefix(ops)
}

class StorylinePrefix[Rdf <: RDF](ops: RDFOps[Rdf]) extends PrefixBuilder("nsl", "http://purl.org/ontology/storyline/")(ops) {
  val Storyline = apply("Storyline")
  val synopsis = apply("synopsis")
  val dateCreated = apply("dateCreated")
}

object CoreConceptsPrefix {
  def apply[Rdf <: RDF](implicit ops: RDFOps[Rdf]) = new CoreConceptsPrefix(ops)
}

class CoreConceptsPrefix[Rdf <: RDF](ops: RDFOps[Rdf]) extends PrefixBuilder("core", "http://www.bbc.co.uk/ontologies/coreconcepts/")(ops) {
  val preferredLabel = apply("preferredLabel")
  val shortLabel = apply("shortLabel")
  val disambiguationHint = apply("disambiguationHint")
}