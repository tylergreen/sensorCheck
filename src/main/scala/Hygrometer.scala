package com.tyler.sensorCheck

sealed abstract class HygrometerRating extends SensorRating
case class Ok extends HygrometerRating 
case class Discard extends HygrometerRating 

class Hygrometer(val name : String, referenceHumidity : Double) extends Sensor { 
  private var rating : SensorRating = Untested()
  def addSample(reading : Double) {
    if (math.abs(reading - referenceHumidity) >= 1)
      rating = Discard()
    else if(rating == Untested())
      rating = Ok()
  }

  def classify : SensorRating = {
    rating
  }
}
