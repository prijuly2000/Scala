

/*
 * @author Pritesh Gandhi
 */
import java.io.File
import scala.collection.mutable.HashMap
import scala.io.Source
object ParCounts {
  
  def main(args: Array[String]): Unit = {

    // Get the list of files present in the directory
    val lof = getFiles(args(0)).par

    // Get the number of cores in the processor
    val cores = Runtime.getRuntime().availableProcessors()

    // Get the start time
    val t0 = System.currentTimeMillis()

    // Find the frequency for each file and merge the partial result 
    // to generate one hashmap with all the frequencies.
    val globalHashMap = lof.map(countWordsByFile).reduce(mergeHashMaps)
    
    val list =globalHashMap.toList
    
    // Sort the list of 2 tuples in descending order of the count of each word
    val sortedList = list.sortWith(_._2 > _._2)

    // Skip the first tuple of the sorted list because it is the number 
    // of spaces occurring in the files. Print the first 10 high frequency words
    for (i <- (1 until 11)) {
      println(sortedList(i)._1 + "  " + sortedList(i)._2)
    }

    // Get the end time
    val t1 = System.currentTimeMillis()

    val tn: Double = (t1 - t0) / 1000.0

    println(" N = " + cores + "    Tn = " + tn)

  }

  def mergeHashMaps(a: HashMap[String, Int], b: HashMap[String, Int]): HashMap[String, Int] = {
    // Convert the map to the list of tuples to merge them the other map
    val list = b.toList
  
    // Merge the list of the map to the other map
    list.foldLeft(a) { (a, p) =>
      val (word, count) = p;
      val newFreq = a.getOrElse(word, 0) + count
      a(word) = newFreq
      a
    }

  }

  def getFiles(dir: String): List[String] = {
    // Get the directory
    val root = new File(dir)
    
    // Create a list of all the file name with the path attached to it
    root.listFiles.filter(_.isFile()).map { f => dir+"\\"+f.getName }.toList
  }

  def countWordsByFile(fileName: String): HashMap[String, Int] = {

    // Create a file object for the current file 
    val file = new File(fileName);
    
    // The lines from the current file
    val lines = Source.fromFile(file).getLines().toList

    // Split each line and increment the count for the each word found in the line
    lines.foldLeft(HashMap[String, Int]()) { (innerMap, line) =>

      // Split the current line into words
      val words = line.toLowerCase().split(" +").toList

      // Increment count of the word  in the wordFrequency
      words.foreach { word =>
        val freq = innerMap.getOrElse(word, 0) + 1
        innerMap(word) = freq
      }
      innerMap
    }
  }
}