package com.tyler.sensorCheck

abstract class SensorRating{
  val format : String
}
case class Untested extends SensorRating{
  val format = "untested"
}

abstract class Sensor{
  val name : String
  def addSample(reading: Double) : Unit
  def classify : SensorRating
}
