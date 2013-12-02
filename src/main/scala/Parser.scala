package com.tyler.sensorCheck
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer


class Parser(input : Array[String]){
  val referenceHumidity = input(0).split(' ')(2).toDouble
  val referenceTemperature = input(0).split(' ')(1).toDouble

  val sensorReadings = Array()

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

