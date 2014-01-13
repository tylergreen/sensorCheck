package com.tyler.sensorCheck

abstract class SensorRating
case class Untested extends SensorRating

abstract class Sensor{
  val name : String
  def addSample(reading: Double) : Unit
  def classify : SensorRating
}
