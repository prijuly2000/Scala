
/*
 * @author pritesh
 */

import java.io.File
import scala.io.Source
import scala.util.control.Breaks
import scala.collection.mutable.Map

object MobyDivideConquer {
  def main(args: Array[String]): Unit = {

    // Call method to start processing the files by spinning an actor per core
    processFiles()
  }

  import scala.actors.Actor._

  /**
   * @param lower
   * @param upper
   * @param fileNames
   * @return
   *
   * Here we calculate the frequency of the words. This function returns a map
   * which stores the frequency of the words found in the processed files.
   */
  def getFrequency(lower: Int, upper: Int, files: List[File]): Map[String, Int] = {

    val fileRange = files.slice(lower, upper)
    val frequency = fileRange.foldLeft(Map[String, Int]() withDefaultValue (0)) { (localMap, file) =>
      val lines = Source.fromFile(file).getLines().toList
      lines.foldLeft(localMap) { (innerMap, line) =>

        // Split the current line into words
        val words = line.toLowerCase().split(" +").toList

        // Increment count of the word  in the wordFrequency
        words.foreach { word =>
          val freq = innerMap.getOrElse(word, 0) + 1
          innerMap(word) = freq
          //innerMap.updated(word, freq)          
        }
        innerMap
      }

    }
    frequency
  }

  /**
   *
   * We spin up an actor with the range of files it is supposed to process. We can
   * change RANGE to spin the number of actors equal to number of files, number of
   * actors equal to number of cores or just one actor. The result returned by the
   * child is merged here after receiving it. In the end, we sort out the map by
   * turning it into a list of two tuples and print the 10 highest frequency words.
   */
  def processFiles() = {

    // Get the number of cores in the processor
    val cores = Runtime.getRuntime().availableProcessors()

    // Get the current time in the beginning
    val t0 = System.currentTimeMillis()

    // Directory where the data is stored 
    val directory = new File("C:\\Users\\pritesh\\Downloads\\moby-13635")

    val caller = self

    // Get all the files from the directory. Filter out if there are any 
    // sub-directories present inside the directory
    val files = directory.listFiles.filter(_.isFile).toList

    /* Change RANGE to make the number of actors equal to :
     * 1) One for RANGE = number of files in the directory, use RANGE = files.length
     * 2) Number of cores for RANGE = ( number of files / number of cores ), use RANGE = (files.length.toDouble / cores).ceil.toInt
     * 3) Number of files  for RANGE = 1
     * */
    val RANGE = (files.length.toDouble / cores).ceil.toInt

    // Find the number of partitions based on the number of RANGE
    val numberOfPartitions = (files.length.toDouble / RANGE).ceil.toInt

    // Spin-up the actors, equal to calculated number of partitions
    for (i <- 0 until numberOfPartitions) {

      // Calculate the lower limit
      val lower = i * RANGE

      // Round the upper limit if current range is greater than length of the list
      val upper = files.length min (i + 1) * RANGE
      actor {
        caller ! getFrequency(lower, upper, files)
      }
    }

    // Here we receive result map of each actor and merge it into one map
    val totalWordsFrequency = (0 until numberOfPartitions).foldLeft(Map[String, Int]()) { (partialFrequency, i) =>
      receive {
        case frequencyInFile: Map[_, _] =>

          // Cast it to the map of specific type
          val childFrequency = frequencyInFile.asInstanceOf[Map[String, Int]]

          childFrequency.foldLeft(partialFrequency) { (acc, p) =>
            var (word,count)=p;
            // If the entry for the word already exists then update it
            // otherwise create a new entry and update it
            val newFreq = acc.getOrElse(word, 0) + count
            acc(word) = newFreq
            acc
          }
      }
    }

    // Convert the totalWordsFrequency map into the list of 2 tuples
    val list = totalWordsFrequency.foldLeft(List[(String, Int)]()) { (list, m) =>
      // Decompose the current map entry into a tuple
      val (word, freq) = m

      // Concatenate the current 2 tuple with the list
      list ++ List((word, freq))
    }

    // Sort the list of 2 tuples in descending order of the count of each word
    val sortedList = list.sortWith(_._2 > _._2)

    // Skip the first tuple of the sorted list because it is the number 
    // of spaces occurring in the files. Print the first 10 high frequency words
    for (i <- (1 until 11)) {
      println(sortedList(i)._1 + "  " + sortedList(i)._2)
    }

    val t1 = System.currentTimeMillis()

    val tn: Double = (t1 - t0) / 1000.0

    println(" N = " + cores + "   Number of Partitions (Actors): " + numberOfPartitions + "    Tn = " + tn)
  }

}