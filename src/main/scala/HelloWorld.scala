/**
  * Created by Vivek Kumar Mishra on 06/06/2018.
  */
object HelloWorld {

  val name:String = "Vivek Kumar MIshra"
  val newName = "Harsha Pandey"
  val value = 50505
  val value2:Int = 101010

  var value3 = 45454
  value3 = 50505
  //String, Int, Char, Boolean, Unit, Double

    //Tuple example
  val t = (1,5.5,"Vivek", true)
  println(t._4)
  println(t)

  def main(args: Array[String]): Unit = {
    println("Hello World")
  }
}
