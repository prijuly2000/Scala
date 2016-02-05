package classes

class Sample {
  println("You are constructing an instance of Sample")
}

object Sample {
  def main(args: Array[String]): Unit = {
    new Sample
  }
}
