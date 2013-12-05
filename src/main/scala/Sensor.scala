package com.tyler.sensorCheck

abstract class Sensor
case class Thermometer(name: String, readings :Array[ Map[String, Any]] ) extends Sensor
case class Hygrometer(name: String, readings :Array[ Map[String, Any]]) extends Sensor

