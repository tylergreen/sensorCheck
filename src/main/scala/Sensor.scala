package com.tyler.sensorCheck
import sensorCheck._
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean

abstract class Sensor{
  def name: String
  def classify: String
}

case class Thermometer(
  name: String,
  reference: Double,
  readings :Array[ (TimeStamp, Double)]
) extends Sensor {
  
  def classify: String = {
    val readingValues = readings.map(_._2)
    val mean = new Mean().evaluate(readingValues)
    val stdDev = new StandardDeviation().evaluate(readingValues)
    val tolerance = math.abs(reference - mean)

    if  (tolerance < 0.5 && stdDev < 3){
      "UltraPrecise"
    }
    else if (tolerance < 0.5 && stdDev < 5){
      "VeryPrecise"
    }
    else {
      "Precise"
    }
  }
}

case class Hygrometer(
  name: String,
  reference: Double,
  readings: Array[ (TimeStamp, Double)]
) extends Sensor {
  
  def classify :String = {
    val outlier = readings.find(reading => math.abs(reading._2 - reference) >= 1)
    if (outlier == None)
      "ok"
    else
      "discard"
  }
   
}

