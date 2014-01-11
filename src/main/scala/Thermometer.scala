package com.tyler.sensorCheck
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean

sealed abstract class ThermometerRating extends SensorRating
case class UltraPrecise extends ThermometerRating {
  val format = "ultra precise"
}
case class VeryPrecise extends ThermometerRating {
  val format = "very precise"
}
case class Precise extends ThermometerRating{
  val format = "precise"
}

class Thermometer(val name : String, referenceTemperature: Double) extends Sensor {
  private val mean = new Mean()
  private val stdDev = new StandardDeviation()

  def addSample(reading : Double){
    mean.increment(reading)
    stdDev.increment(reading)
  }

  def classify: SensorRating = {
    val tolerance = math.abs(referenceTemperature - mean.getResult)
    val stdDevResult = stdDev.getResult

    //I favor flat, unnested logic for clarity
    if (tolerance < 0.5 && stdDevResult < 3){
      UltraPrecise()
    }
    else if (tolerance < 0.5 && stdDevResult < 5){
      VeryPrecise()
    }
    else {
      Precise()
    }
  }
}
