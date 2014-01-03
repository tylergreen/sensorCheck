package com.tyler.sensorCheck

import scala.util.parsing.combinator._
import sensorCheck._

abstract class InputLine
case class Reference(temperature: Double, humidity: Double) extends InputLine
case class ThermometerDeclaration(sensorName: String) extends InputLine
case class HygrometerDeclaration(sensorName: String) extends InputLine
case class Reading(sensorName: String, time: TimeStamp, quantity: Double) extends InputLine
case class Unknown(line: String) extends InputLine

class PrimitiveParser extends RegexParsers {
  def number: Parser[Double] = """\d+(\.\d*)?""".r ^^ { _.toDouble }
  def date: Parser[String] = """\d{4}\-\d{2}\-\d{2}""".r ^^ { _.toString }
  def time: Parser[String] = """T\d{2}:\d{2}""".r ^^ { _.toString }
  def timestamp: Parser[String] = date ~ time ^^ { case ~(date1, time1) => date1 ++ time1 }
  def identifier: Parser[String] = """[\w\-]+""".r ^^ {_.toString}
}

object LineParser extends PrimitiveParser {
  def reference: Parser[InputLine] = ("reference" ~ number ~ number) ^^ {
    case "reference" ~ temperature ~ humidity => Reference(temperature, humidity)
  }

  def thermometerDeclaration: Parser[InputLine] = "thermometer " ~ identifier ^^ { 
    case ~(_, sensorId) => ThermometerDeclaration(sensorId)
  }
  
  def hygrometerDeclaration: Parser[InputLine] = "humidity " ~ identifier ^^ {
    case ~(_, sensorId) => HygrometerDeclaration(sensorId)
  }
  
  def reading: Parser[InputLine] = timestamp ~ identifier ~ number ^^ {
    case timestamp ~ sensorId ~ quantity => Reading(sensorId, timestamp, quantity)
  }

  def unknown : Parser[InputLine] = """.*""".r ^^ { Unknown(_) }
     
  def line: Parser[InputLine] = reference | thermometerDeclaration | hygrometerDeclaration | reading | unknown

  def parse(str: String) : InputLine = {
    parseAll(line, str).get
  }

}
