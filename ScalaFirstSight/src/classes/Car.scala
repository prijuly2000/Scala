package classes

import scala.collection.mutable.ListBuffer

class Car(val year: Int) {
  private val trips = new ListBuffer[Int]()

  def miles() = trips.reduce{ (a,b) => a + b}

  def drive(distance: Int) {
//    trips += distance
    trips.append(distance)
  }

}

object Car {
  def main(args: Array[String]): Unit = {
    val car = new Car(2015)
    
    car.drive(10)
    car.drive(5)

    println("year = " + car.year + " miles = " + car.miles)
  }
}