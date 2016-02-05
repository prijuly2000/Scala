package matching

/**
 * @author Ron.Coleman
 */
/*object MatchLiterals {
  def main(args: Array[String]): Unit = {
    List("Monday", "Sunday", "Saturday").foreach { activity }
  }

  def activity(day: String) {
    day match {
      case "Sunday"   => print("Eat, sleep, repeat... ")
      case "Saturday" => print("Hangout with friends... ")
      case "Monday"   => print("...code for fun...")
      case "Friday"   => print("...read a good book...")
    }
  }

}*/

object MainObject {
  def main(arr:Array[String]){
      List("Sunday","Monday","Tuesday","Wednesdasy").foreach (activity)
    }
  def activity(day:String){
    day match {
      case "Sunday"=> println("whats up")
      case "Monday" => println("hahaha")
      case "Saturday"=> println("kajsdlkjf")
      case _ => println("nothing")
    }
    
  }
}