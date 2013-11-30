package com.tyler.sensorCheck

class Parser(input : Array[String]){
  val referenceHumidity = input(0).split(' ')(2).toDouble
  val referenceTemperature = input(0).split(' ')(1).toDouble
}
