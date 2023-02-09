package uk.gov.nationalarchives

import com.google.common.jimfs.{Configuration, Jimfs}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{FileSystem, Files, Path}

class CsvProcessorSpec extends AnyFlatSpec with should.Matchers {
  val fs: FileSystem = Jimfs.newFileSystem("FileSourceSpec", Configuration.unix())

  private def writeCsv(csvMap: Map[String, String]): Path = {
    val file = Files.createTempFile(fs.getPath("/"), "test-file", ".csv")
    val text = "filename,checksum\n" + csvMap.map(c => s"${c._1},${c._2}").mkString("\n")
    Files.newBufferedWriter(file, UTF_8).append(text).close()
    file
  }

  "getDuplicatesFromFile" should "return one set of two files" in {
    val input = Map(
      "file1" -> "checksum1",
      "file2" -> "checksum1",
      "file3" -> "checksum2"
    )
    val fileName = writeCsv(input)
    val duplicates = CsvProcessor().getDuplicatesFromFile(fileName)

    duplicates.size should equal(1)
    duplicates.head.sorted should equal(List("file1", "file2"))
  }

  "getDuplicatesFromFile" should "return two sets of two files" in {
    val input = Map(
      "file1" -> "checksum1",
      "file2" -> "checksum1",
      "file3" -> "checksum2",
      "file4" -> "checksum3",
      "file5" -> "checksum3"
    )
    val fileName = writeCsv(input)
    val duplicates = CsvProcessor().getDuplicatesFromFile(fileName)

    duplicates.size should equal(2)
    duplicates.head.sorted should equal(List("file1", "file2"))
    duplicates.last.sorted should equal(List("file4", "file5"))
  }

  "getDuplicatesFromFile" should "return one set of two files and one set of four files" in {
    val input = Map(
      "file1" -> "checksum1",
      "file2" -> "checksum1",
      "file3" -> "checksum2",
      "file4" -> "checksum3",
      "file5" -> "checksum3",
      "file6" -> "checksum3",
      "file7" -> "checksum3"
    )
    val fileName = writeCsv(input)
    val duplicates = CsvProcessor().getDuplicatesFromFile(fileName)

    duplicates.size should equal(2)
    duplicates.head.sorted should equal(List("file1", "file2"))
    duplicates.last.sorted should equal(List("file4", "file5", "file6", "file7"))
  }

  "getDuplicatesFromFile" should "return three sets of two files" in {
    val input = Map(
      "file1" -> "checksum1",
      "file2" -> "checksum1",
      "file3" -> "checksum2",
      "file4" -> "checksum3",
      "file5" -> "checksum3",
      "file6" -> "checksum4",
      "file7" -> "checksum4"
    )
    val fileName = writeCsv(input)
    val duplicates = CsvProcessor().getDuplicatesFromFile(fileName).sortBy(_.head)

    duplicates.size should equal(3)
    duplicates.head.sorted should equal(List("file1", "file2"))
    duplicates(1).sorted should equal(List("file4", "file5"))
    duplicates.last.sorted should equal(List("file6", "file7"))
  }

  "getDuplicatesFromFile" should "return zero files" in {
    val input = Map(
      "file1" -> "checksum1",
      "file2" -> "checksum2",
      "file3" -> "checksum3",
      "file4" -> "checksum4"
    )
    val fileName = writeCsv(input)
    val duplicates = CsvProcessor().getDuplicatesFromFile(fileName).sortBy(_.head)

    duplicates.size should equal(0)
  }
}
