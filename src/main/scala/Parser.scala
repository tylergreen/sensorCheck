package com.tyler.sensorCheck
import scala.annotation.tailrec


class Parser(input : Array[String]){
  val referenceHumidity = input(0).split(' ')(2).toDouble
  val referenceTemperature = input(0).split(' ')(1).toDouble

  val sensorReadings = Array()


  def splitSections (
    isSeparator: Any => Boolean,
    list: List[Any],
    accum: List[List[Any]]
  ) : List[List[Any]] = {
    if (list == Nil) {
      accum.reverse
    }
    else {
      val header = list.head
      val rest = list.tail
      val (section, residue) = rest.span(x => !isSeparator(x))
      splitSections(isSeparator, residue,  (header :: section) :: accum )
    }
  }
  
}
