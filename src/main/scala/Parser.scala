package com.tyler.sensorCheck
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import com.tyler.sensorCheck.Input

class Parser(input : Array[String]){
  
  var referenceLine = input(0)
  var referenceHumidity = referenceLine.split(' ')(2).toDouble
  var referenceTemperature = referenceLine.split(' ')(1).toDouble

  
  var rawReadings = input.drop(1)
  var sensorNameColumn = 1
  var sensors = rawReadings
    .map((line: String) => line.split(" "))
    .groupBy(_(sensorNameColumn))
    .values.map(parseSection(_))
    .toArray

  def parseSection(section : Array[Array[String]]) : Sensor = {
    val Array(sensorType, sensorName) = section(0)
    val readings = section.drop(1).map(line =>
      (line(0), line(2).toDouble)
    )
    sensorType match {
      case "thermometer" => Thermometer(sensorName, referenceTemperature, readings)
      case "humidity" => Hygrometer(sensorName, referenceHumidity, readings)
    }
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

}

