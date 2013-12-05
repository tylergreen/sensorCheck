package com.tyler.sensorCheck
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.Parser
import com.tyler.sensorCheck.Sensor

object SensorCheck{
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



