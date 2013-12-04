package com.tyler.sensorCheck
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean

object ThermometerCheck {
  def classify(reference : Double, readings : Array[Double]) : String = {
    val mean = new Mean().evaluate(readings)
    val stdDev = new StandardDeviation().evaluate(readings)
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
