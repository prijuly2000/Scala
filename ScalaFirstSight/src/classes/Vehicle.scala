package classes

/**
 * @author Ron.Coleman
 */
class Vehicle(val id: Int, val year: Int) {
  override def toString(): String = "ID: " + id + " Year: " + year
}

class MyCar(override val id: Int, override val year: Int, var fuelLevel: Int) extends Vehicle(id, year) {
  override def toString(): String = super.toString() + " Fuel Level: " + fuelLevel
}

object Vehicle {
  def main(args: Array[String]): Unit = {
    val myCar = new MyCar(1, 2015, 100)

    println(myCar)
  }
}