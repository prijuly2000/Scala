package odds

/**
 * @author Ron.Coleman
 */
object InnerDefs {
  def main(args: Array[String]): Unit = {
    val nums = List(1,2,3,4)
    
    def hasEven(): Boolean = {
      nums.filter { n => n % 2 == 0}.length != 0
    }
    
    def foo(f: () => Boolean): Unit = {
      if(f())
        println("has evens!")
      else
        println("does not have evens!")
    }
    
    foo(hasEven)
  }
}