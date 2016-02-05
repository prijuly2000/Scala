

package basics
/**
 * @author Ron.Coleman
 */
object Reduce {
  def main(args: Array[String]): Unit = {
    val nums = List(1, 3, 4, 5, 12, 2, 7, 9)

    val sum = nums.reduce { (a, b) => a + b }
    println(sum)
    println(nums.reduce(_ + _))

  }
}