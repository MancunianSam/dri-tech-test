# Duplicate file processor 

This project contains a method `getDuplicatesFromFile` in the `CsvProcessor` class. 
This takes a `Path` to a CSV file and returns a `List[List[String]]` of files which have the same checksum.
This uses Akka streams. The processing steps are:
* Load the CSV file into memory
* Scan the lines of the file
* Convert each line to a `Map[String,String]` with the column header as the key.
* Fold the lines into `Map[String, List[String]]` where the key is the checksum and the value is a list of files with that checksum.
* Filters out only those entries where there is more than one file.

## Running the code
The code can be run using sbt.
```bash
sbt "run /path/to/example.csv"
```

It can also be run from within IntelliJ by creating an Application runner to run the `Runner` class and passing the file path as a program argument.

## Running the tests
The tests  are run using Scala Test. They use [Jimfs](https://github.com/google/jimfs) to create an in memory file system. 
They can be run using sbt.
```bash
sbt test
```

They can also be run through IntelliJ through a ScalaTest configuration.
