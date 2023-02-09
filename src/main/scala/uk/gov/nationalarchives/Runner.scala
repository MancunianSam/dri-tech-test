package uk.gov.nationalarchives

import java.nio.file.Paths

object Runner extends App {
  println(CsvProcessor().getDuplicatesFromFile(Paths.get(args.head)))
}
