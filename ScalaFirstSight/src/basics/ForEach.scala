package basics/**
 * Demonstrates val, list, and foreach.
 * @author Ron.Coleman
 */
object ForEach {
  def main(args: Array[String]): Unit = {
    val nums = List(1, 3, 4, 5, 12, 2, 7, 9)

    nums.foreach { n =>
      println(n)
    }
  }
}