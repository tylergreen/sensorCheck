package com.tyler.sensorCheck
import scala.io.Source

object Input {
  def lines : Array[String] = {
     return io.Source.fromFile("testInput.txt").getLines.toArray
  }

}
