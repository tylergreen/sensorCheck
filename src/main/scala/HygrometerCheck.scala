package com.tyler.sensorCheck

object HygrometerCheck {
  def classify(reference: Double, readings : Array[Double]) : String = {
    val outlier = readings.find(reading => math.abs(reading - reference) >= 1)
    if (outlier == None)
      "ok"
    else 
      "discard"
  }

}
