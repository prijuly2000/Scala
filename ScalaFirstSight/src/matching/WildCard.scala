package matching

/**
 * @author Ron.Coleman
 */
object WildCard {
  object DayOfWeek extends Enumeration {
    val SUNDAY = Value("Sunday")
    val MONDAY = Value("Monday")
    val TUESDAY = Value("Tuesday")
    val WEDNESDAY = Value("Wednesday")
    val THURSDAY = Value("Thursday")
    val FRIDAY = Value("Friday")
    val SATURDAY = Value("Saturday")
  }

  def activity(day: DayOfWeek.Value){
    day match {
      case DayOfWeek.SUNDAY   => println("Eat, sleep, repeat...")
      case DayOfWeek.SATURDAY => println("Hangout with friends")
      case _                  => println("...code for fun...")
    }
  }
  
  def main(args: Array[String]): Unit = {
    activity(DayOfWeek.SATURDAY)
    activity(DayOfWeek.MONDAY)
  }
}