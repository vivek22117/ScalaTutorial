package chp3SparkRDD

import chp1.TempData
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Vivek Kumar Mishra on 30/07/2018.
  */
object TempDataRDD {

  def main(args: Array[String]): Unit = {
     val conf = new SparkConf().setAppName("Temp Data").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("data/tempData.csv").filter(line => !line.contains("Day"))
    val tempData = lines.flatMap{ data =>
      val p = data.split(",")
      if(p(7) == "." || p(8) == "." || p(9) == ".") Seq.empty else
        Seq(TempData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, TempData.toDoubleOrNeg(p(5)),
          TempData.toDoubleOrNeg(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }

    tempData.take(5) foreach(println)
  }
}
