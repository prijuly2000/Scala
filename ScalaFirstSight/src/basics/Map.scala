

package basics/**
 * @author Ron.Coleman
 */
object Map {
  def main(args: Array[String]): Unit = {
    val nums = List(1, 3, 4, 5, 12, 2, 7, 9)

    val times2 = nums.map { n => n * 2}
    times2.foreach { n =>
      println(n)
    }
  }
}