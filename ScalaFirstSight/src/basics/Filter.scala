

package basics/**
 * @author Ron.Coleman
 */
object Filter {
  def main(args: Array[String]): Unit = {
    val nums = List(1, 3, 4, 5, 12, 2, 7, 9)

    val evens = nums.filter { n => n % 3 == 0}
    evens.foreach { n =>
      println(n)
    }
  }
}