package com.tyler.sensorCheck

abstract class Sensor{
  val name : String
  def add(reading: Double) : Unit
  def classify : String
}


