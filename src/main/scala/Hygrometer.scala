package com.tyler.sensorCheck

class Hygrometer(val name : String, referenceHumidity : Double) extends Sensor { 
  private var rating : String = "OK"
  def addSample(reading : Double) {
    if (math.abs(reading - referenceHumidity) >= 1)
      rating = "discard"
  }

  def classify : String = {
    s"$name: $rating"
  }
}
