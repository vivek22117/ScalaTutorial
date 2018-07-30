package chp2

import chp1.TempData


/**
  * Created by Vivek Kumar Mishra on 30/07/2018.
  */
object ScalaParallelism {

  def toDoubleOrNeg(str: String): Double = {
    try{
      str.toDouble
    } catch {
      case _:NumberFormatException => -1
    }
  }

  def main(args: Array[String]): Unit ={

    val source = scala.io.Source.fromFile("data/tempData.csv")
    val lines = source.getLines().drop(1)
    val tempData = lines.flatMap{ data =>
      val p = data.split(",")
      if(p(7) == "." || p(8) == "." || p(9) == ".") Seq.empty else
        Seq(TempData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, toDoubleOrNeg(p(5)), toDoubleOrNeg(p(6)),
          p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }.toArray

    val startTime = System.currentTimeMillis()
    val (rainyTemps, rainyDays) = tempData.par.aggregate(0.0,0)({ case((sum, cnt), td) =>
      if(td.precp < 1.0) (sum, cnt) else (sum + td.tmax, cnt + 1)
    }, {
      case((s1, c1) , (s2,c2)) => (s1 + s2, c1 + c2)
    })

    println(s"Average rainy temps is: ${rainyTemps/rainyDays}")
    println(s"Time take is: ${System.currentTimeMillis() - startTime}")

    source.close()
    tempData.take(5) foreach(println)

  }

}
