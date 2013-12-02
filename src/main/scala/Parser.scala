package com.tyler.sensorCheck
import scala.annotation.tailrec


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
      accum: List[List[Any]]
    ) : List[List[Any]] = {
      if (xs == Nil)
        accum.reverse
      else {
        val header :: rest = xs
        val (section, residue) = rest.span(x => !isSeparator(x))
        aux(residue,  (header :: section) :: accum )
      }
    }
    aux(list, Nil)
  }
}

