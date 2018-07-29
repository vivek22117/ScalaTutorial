package chp1

/**
  * Created by Vivek Kumar Mishra on 29/07/2018.
  * Examaple of using Standard Scala API's to perform transformations.
  * Data is not available in github repo
  * Data structure is below:
  *
  * Day  ,JD  ,Month  ,State_id  ,Year  ,PRCP (in),SNOW (in),TAVE (F),TMAX (F),TMIN (F)
    1,335,12,'212142',1895,0,0,12,26,-2
    2,336,12,'212142',1895,0,0,-3,11,-16
    3,337,12,'212142',1895,0,0,6,11,0
    14,14,1,'212142',1952,.,.,.,.,.
    7,220,8,'212142',1916,0.52,.,70,80,59
    8,221,8,'212142',1916,0,.,68,83,53
    9,222,8,'212142',1916,0,.,70,81,58
  */

case class TempData(day: Int, doy: Int, month: Int, year:Int, precp:Double,
                    snow:Double, tavg:Double, tmax:Double, tmin:Double)

object TempData {

  def toDoubleOrNeg(s: String): Double = {
    try{
      s.toDouble
    } catch {
      case _:NumberFormatException => -1
    }
  }

    def main(args: Array[String]): Unit ={
      val source = scala.io.Source.fromFile("data/tempData.csv")
      val lines = source.getLines().drop(1)
      val tempData = lines.flatMap {data =>
        val p = data.split(",")
        if(p(7) == "." || p(8) == "." || p(9) == ".") Seq.empty else
        Seq(TempData(p(0).toInt,p(1).toInt, p(2).toInt, p(4).toInt, toDoubleOrNeg(p(5)),
          toDoubleOrNeg(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
      }.toArray

      source.close()

      val tMax = tempData.map(data => data.tmax).max
      val hotDays = tempData.filter(data => data.tmax == tMax)
      println(s"hot days are ${hotDays.mkString}")

      val hotDay = tempData.maxBy(data => data.tmax)
      println(s"hot day 1 is $hotDay")

      val hotDay2 = tempData.reduceLeft((d1, d2) => if(d1.tmax >= d2.tmax) d1 else d2)
      println(s"hot day in 2 is $hotDay2")

      val rainyCount = tempData.count(data => data.precp > 1.0)
      println(s"There are $rainyCount rainy days. And aveg temp is ${rainyCount * 100.0 / tempData.length}")

      val (rainlyTempSum, rainyCount2) = tempData.foldLeft((0.0, 0)) { case ((sum, cnt), data) =>
        if(data.precp < 1.0) (sum, cnt) else (sum + data.tmax, cnt + 1)
      }
      println(s"Average temp data is ${rainlyTempSum / rainyCount2}")

      val rainyTemps = tempData.flatMap(data => if(data.precp < 1.0) Seq.empty else Seq(data.tmax))
      println(s"Average rainly temp is ${rainyTemps.sum / rainyTemps.length}")

      val monthGroups = tempData.groupBy(data => data.month)
      val monthlyTemp = monthGroups.map{ case(m, days) =>
        m -> days.foldLeft(0.0)((sums, td) => sums + td.tmax / days.length)
      }

      monthlyTemp.toSeq.sortBy(data => data._1) foreach(println)


      println(tempData.length)
      tempData.take(5) foreach(println)
    }
}
