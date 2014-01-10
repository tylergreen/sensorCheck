package com.tyler.sensorCheck
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean

class Thermometer(val name : String, referenceTemperature: Double) extends Sensor {
  val mean = new Mean()
  val stdDev = new StandardDeviation()

  def addSample(reading : Double){
    mean.increment(reading)
    stdDev.increment(reading)
  }

  def classify: String = {
    val tolerance = math.abs(referenceTemperature - mean.getResult)

    val stdDevResult = stdDev.getResult
    val rating = if  (tolerance < 0.5 && stdDevResult < 3){
      "ultra precise"
    }
    else if (tolerance < 0.5 && stdDevResult < 5){
      "very precise"
    }
    else {
      "precise"
    }
    s"$name: $rating"
  }
}
