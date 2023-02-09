package uk.gov.nationalarchives

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.{FileIO, Sink}
import com.typesafe.scalalogging.Logger

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

class CsvProcessor() {
  private val actorSystem: ActorSystem = ActorSystem()
  implicit val materialiser: Materializer = Materializer(actorSystem)
  implicit val ec: ExecutionContextExecutor = materialiser.executionContext
  private val logger: Logger = Logger("CsvProcessor")

  def getDuplicatesFromFile(path: Path): List[List[String]] = {
    logger.info(s"Processing file $path")

    val result = FileIO.fromPath(path)
      .via(CsvParsing.lineScanner())
      .via(CsvToMap.toMapAsStrings(StandardCharsets.UTF_8))
      .fold(Map[String, List[String]]())((acc, row) => {
        val checksum: String = row("checksum")
        val fileNameList: List[String] = row("filename") :: acc.getOrElse(checksum, Nil)
        acc + (checksum -> fileNameList)
      })
      .map(_.values.filter(_.size > 1).toList)
      .runWith(Sink.head)

    result.onComplete(_ => actorSystem.terminate())
    Await.result(result, 10.seconds)
  }
}
object CsvProcessor {
  def apply() = new CsvProcessor()
}
