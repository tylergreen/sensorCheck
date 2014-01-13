package com.tyler.sensorCheck

abstract class Output
case class ErrorMsg(message : String) extends Output {
  override def toString = s"Error: $message"
}
case class Rating(sensorId : String, rating : SensorRating) extends Output {
  override def toString = rating match {
    case UltraPrecise() => s"$sensorId: ultra precise"
    case VeryPrecise() => s"$sensorId: very precise"
    case Precise() => s"$sensorId: precise"
    case Ok() => s"$sensorId: OK"
    case Discard() => s"$sensorId: discard"
    case Untested() => s"$sensorId: untested"
  }
}


