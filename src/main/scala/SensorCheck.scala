package com.tyler.sensorCheck
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean


object SensorCheck {
  def run(input: String) : List[String] = {
    val lines = input.split('\n').map(LineParser.parse(_))
    val Reference(temp, hum) = lines(0)
    val ThermometerDeclaration(name) = lines(1)
    val readings = lines.drop(2).map{case Reading(_, _, r) => r }
    val stdDev = new StandardDeviation().evaluate(readings)
    val mean = new Mean().evaluate(readings)
    val tolerance = math.abs(temp - mean)
    val rating = 
      if  (tolerance < 0.5 && stdDev < 3){
        "ultra precise"
      }
      else if (tolerance < 0.5 && stdDev < 5){
        "very precise"
      }
      else {
        "precise"
      }

    List(name + ": " + rating)
  }



  def main(args: Array[String]){
    println("start")
    var lines = io.Source.stdin.getLines
    //var sensorLog = lines.mkString("\n")
    var parser = new Parser(lines.toArray) //change parser to take an iterator
    var output = parser.sensors.map(x => x.name + ": " + x.classify)
    for (ln <- output) println(ln)
  }
  // def checkReadings(
  //   referenceTemperature : Double,
  //   referenceHumidity : Double,
  //   readings : Array[Map[String, String]]){
  //   readings.map(x => x("header") match {
  //     case Array("humidity", sensorName) => sensorName + ": " + HygrometerCheck.classify(referenceHumidity, x._2)
  //     case Array("temperature", sensorName) => sensorName + ": " + ThermometerCheck.classify(referenceTemperature, x._2)
  //   }
  //   )
  // }
}



