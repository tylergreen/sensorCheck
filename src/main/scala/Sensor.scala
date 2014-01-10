package com.tyler.sensorCheck

abstract class Sensor{
  val name : String
  def addSample(reading: Double) : Unit
  def classify : String
}


