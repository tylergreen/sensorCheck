package com.tyler.sensorCheck
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

object SensorCheck {

  case class State(currentSensor : Option[Sensor], sensorName : String, results : List[String])

  def run(input: String) : List[String] = {
    val lines = input.split('\n').map(LineParser.parse(_))
    val Reference(referenceTemp, referenceHum) = lines(0)

    val initialState = State(None, "",  List() : List[String])
    val State(lastSensor, lastId, resultsList : List[String]) = lines.tail.foldLeft(initialState){ case (State(sensor, name, results), line) =>
      line match {
        case ThermometerDeclaration(sensorId) => 
          val newSensor = new ThermometerCheck(referenceTemp)
          sensor match {
            case Some(prevSensor) =>
              State(Some(newSensor), sensorId, (name + ": " + prevSensor.classify) :: results)
            case None =>
              State(Some(newSensor), sensorId, results)
          }
        case HygrometerDeclaration(sensorId) =>
          val newSensor = new HygrometerCheck(referenceHum)
          sensor match {
            case Some(prevSensor) =>
              State(Some(newSensor), sensorId, (name + ": " + prevSensor.classify) :: results)
            case None =>
              State(Some(newSensor), sensorId, results)
          }
        case Reading(_,_,quantity) => 
          sensor.get.add(quantity)
          State(sensor, name, results)
      }
    }

    ((lastId + ": " + lastSensor.get.classify) :: resultsList).reverse

  }

}



