package com.tyler.sensorCheck

abstract class HygrometerRating {
  val format : String
}
case object Ok extends HygrometerRating {
  val format = "ok"
}
case object Discard extends HygrometerRating {
  val format = "discard"
}

class HygrometerCheck(referenceHumidity : Double) extends Sensor { 
  private var rating : String = "OK"
  def add(reading : Double) {
    if (math.abs(reading - referenceHumidity) >= 1)
      rating = "discard"
  }

  def classify : String = {
    rating
  }
}
