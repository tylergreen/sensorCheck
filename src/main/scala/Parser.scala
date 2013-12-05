package com.tyler.sensorCheck
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import com.tyler.sensorCheck.Input

class Parser(input : Array[String]){
  val referenceHumidity = input.head.split(' ')(2).toDouble
  val referenceTemperature = input.head.split(' ')(1).toDouble

  val referenceLine = input(0)
  val readings = input.drop(1)
  val sensorNameColumn = 1
  val sensorReadings = readings
    .map((x:String) => x.split(" "))
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

  // splits a list into sections
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

