package com.tyler.sensorCheck
import sensorCheck._

object Printer {

  def print(output : Output) : String = {
    output match { 
      case Left(errorMsg) => s"Error: $errorMsg"
      case Right((sensorId, sensorRating)) =>
        sensorRating match {
          case UltraPrecise() => s"$sensorId: ultra precise"
          case VeryPrecise() => s"$sensorId: very precise"
          case Precise() => s"$sensorId: precise"
          case Ok() => s"$sensorId: OK"
          case Discard() => s"$sensorId: discard"
          case Untested() => s"$sensorId: untested"
        }
    }
  }
}



