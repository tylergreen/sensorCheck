package com.tyler.sensorCheck
import sensorCheck._

abstract class InputLine
case class Reference(temperature: Double, humidity: Double) extends InputLine
case class ThermometerDeclaration(name: String) extends InputLine
case class HygrometerDeclaration(name: String) extends InputLine
case class Reading(sensorName: String, time: TimeStamp, quantity: Double) extends InputLine
case class Unknown(line: String) extends InputLine
case class Eof() extends InputLine
