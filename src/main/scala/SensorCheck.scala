package com.tyler.sensorCheck
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean


object SensorCheck {
  def run(input: String) : List[String] = {
    val lines = input.split('\n').map(LineParser.parse(_))
    val Reference(referenceTemp, referenceHum) = lines(0)
    val readings : Seq[Double] = lines.drop(2).map{case Reading(_, _, r) => r }
    lines(1) match {
      case ThermometerDeclaration(name) => classifyThermometer(name, referenceTemp, readings)
      case HygrometerDeclaration(name) => classifyHygrometer(name, referenceHum, readings)
    }
  }

  def classifyThermometer(name : String, referenceTemp : Double, readings : Seq[Double]) : List[String] = {
    val stdDev = new StandardDeviation().evaluate(readings.toArray)
    val mean = new Mean().evaluate(readings.toArray)
    val tolerance = math.abs(referenceTemp - mean)
    val rating = 
      if (tolerance < 0.5 && stdDev < 3){
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

  def classifyHygrometer(name : String, referenceHum : Double, readings : Seq[Double] ) : List[String] = {
    val tolerance = 1
    if (readings.forall(reading => tolerance >= math.abs(reading - referenceHum)))
      List(name + ": OK")
    else
      List(name + ": discard")
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



