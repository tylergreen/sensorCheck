package com.tyler.sensorCheck
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean

sealed abstract class ThermometerRating{
  val format : String
}
case object UltraPrecise extends ThermometerRating {
  val format = "ultra precise"
}
case object VeryPrecise  extends ThermometerRating {
  val format = "very precise"
}
case object Precise extends ThermometerRating {
  val format = "precise"
}

class ThermometerCheck(val name : String, referenceTemperature: Double) extends Sensor {
  val mean = new Mean()
  val stdDev = new StandardDeviation()

  def add(reading : Double){
    mean.increment(reading)
    stdDev.increment(reading)
  }

  def classify: String = {
    val tolerance = math.abs(referenceTemperature - mean.getResult)

    val stdDevResult = stdDev.getResult
    if  (tolerance < 0.5 && stdDevResult < 3){
      //UltraPrecise
      "ultra precise"
    }
    else if (tolerance < 0.5 && stdDevResult < 5){
      "very precise"
    }
    else {
      "precise"
    }
  }
}
