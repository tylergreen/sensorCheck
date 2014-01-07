package com.tyler.sensorCheck
import scala.util.control.Exception._
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

object SensorCheck {

  case class State(currentSensor : Sensor, sensorName : String, results : List[String])

  def run(input: String) : List[String] = {
    
    val lines = input.split('\n').map(LineParser.parse(_))
    val Reference(referenceTemp, referenceHum) = lines(0)

    val firstDeclaration = lines(1)
    val initialState = firstDeclaration match {
      case SensorDeclaration(Thermometer(name)) =>
        State(new ThermometerCheck(referenceTemp), name, List() : List[String])
      case SensorDeclaration(Hygrometer(name)) =>
        State(new HygrometerCheck(referenceHum), name, List() : List[String])
      case _ => throw new RuntimeException("Second line must be a SensorDeclaration")
    }

    val logBody = lines.drop(2)
    val State(lastSensor, lastId, resultsList : List[String]) = logBody.foldLeft(initialState){ 
      case (State(sensor, name, results), line) =>
        line match {
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            State(newSensor, newId, (name + ": " + sensor.classify) :: results)
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            State(newSensor, newId, (name + ": " + sensor.classify) :: results)
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            State(sensor, name, results)
          case Unknown(line) =>
            throw new RuntimeException("unknown line: " + line)
        }
    }

    ((lastId + ": " + lastSensor.classify) :: resultsList).reverse

  }

}



