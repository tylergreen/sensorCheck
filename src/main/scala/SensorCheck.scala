package com.tyler.sensorCheck
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

import scala.collection.mutable.ListBuffer
import scala.annotation.tailrec


import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean


object SensorCheck {
  def run(input: String) : List[String] = {
    val lines = input.split('\n').map(LineParser.parse(_))
    val Reference(referenceTemp, referenceHum) = lines(0)
    //val readings : Seq[Double] = lines.drop(2).map{case Reading(_, _, r) => r }

//    val stdDev = new StandardDeviation().evaluate(readings.toArray)
//    val mean = new Mean().evaluate(readings.Toarray)

    val ThermometerDeclaration(name1) = lines(1)
    val resultsList1 : List[String] = List()
    val initialState = (new ThermometerCheck(referenceTemp), name1,  resultsList1)
    val (lastSensor, lastId, resultsList : List[String]) = lines.drop(2).foldLeft(initialState){ case ((sensor, name, results), line) =>
      line match {
        case ThermometerDeclaration(sensorId) => 
          val new_sensor = new ThermometerCheck(referenceTemp)
          (new_sensor, sensorId, (name + ": " + sensor.classify.format) :: results)
        case Reading(_,_,quantity) => 
          sensor.add(quantity)
          (sensor, name, results)
      }
    }

    ((lastId + ": " + lastSensor.classify.format) :: resultsList).reverse





    //  }
    // val sensorSections = splitSections({
    //   case ThermometerDeclaration(_) => true
    //   case HygrometerDeclaration(_) => true
    //   case _ => false
    // }, lines.drop(1).toList)

    // sensorSections.map{ case sensorDeclaration :: readings  =>
    //   val quantities = readings.map{ case Reading(_,_,quantity) => quantity }
    //   sensorDeclaration match {
    //     case ThermometerDeclaration(name) => classifyThermometer(name, referenceTemp, quantities)
    //     case HygrometerDeclaration(name) => classifyHygrometer(name, referenceHum, quantities)
    //   }
    // }
  }

  def classifyThermometer(name : String, referenceTemp : Double, readings : Seq[Double]) : String = {
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
    name + ": " + rating
  }

  def classifyHygrometer(name : String, referenceHum : Double, readings : Seq[Double] ) : String = {
    val tolerance = 1
    if (readings.forall(reading => tolerance >= math.abs(reading - referenceHum)))
      name + ": OK"
    else
      name + ": discard"
  }

  // No longer used, but I'm keeping it around for reference
  // utility method that splits a list into sections
  // sections consist of a header (defined by isSeparator) and all the elements until the next header
  def splitSections (
    isSeparator: Any => Boolean,
    list: List[Any]
  ) : List[List[Any]] = {

    @tailrec def aux(
      xs: List[Any],
      accum: ListBuffer[List[Any]]
    ) : List[List[Any]] = {
      if (xs == Nil)
        accum.toList
      else {
        val header :: rest = xs
        val (section, residue) = rest.span(x => !isSeparator(x))
        aux(residue,  accum += (header :: section))
      }
    }
    aux(list, ListBuffer.empty[List[Any]])
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



