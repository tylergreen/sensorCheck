package com.tyler.sensorCheck
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

case class IState(
  currentSensor : Option[Sensor],
  reference : Option[Reference])

object StateTransitions {

  val initialState = IState(None, None)
  type Output = String

  def nextState : (IState, InputLine) => (IState, Option[Output]) = {
    { (state : IState, line: InputLine) =>
      state match {
        case s@IState(None, None) => setReference(s, line)
        case s@IState(None, Some(_)) => setFirstSensor(s,line)
        case s@IState(Some(_), Some(_)) => handleRestLines(s, line)
      }
    }
  }

  private def setReference(state : IState, line : InputLine) : (IState, Option[Output]) = {
    state match {
      case IState(None, None) =>
        line match {
          case r@Reference(referenceTemperature, referenceHumidity) =>
            (IState(None, Some(r)),
              None)
          case _ =>
            (IState(None, None),
              Some("Error: First Line must be reference"))
          }
    }
  }

  private def setFirstSensor(state : IState, line : InputLine) : (IState, Option[Output]) = {
    state match {
      case IState(None, ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case HygrometerDeclaration(sensorName) =>
            val newSensor = new HygrometerCheck(sensorName, referenceHum)
            (IState(Some(newSensor), ref),
              None)
          case ThermometerDeclaration(sensorName) =>
            val newSensor = new ThermometerCheck(sensorName, referenceTemp)
            (IState(Some(newSensor), ref),
              None)
          case _ =>
            (IState(None, ref),
              Some("Error: Second Line must be a sensor declaration ")
            )
        }
    }
  }

  private def handleRestLines(state: IState, line: InputLine) : (IState,Option[Output]) = {
    state match {
      case IState(Some(sensor), ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case ThermometerDeclaration(sensorName) =>
            val newSensor = new ThermometerCheck(sensorName, referenceTemp)
            (IState(Some(newSensor), ref),
              Some(sensor.classify))
          case HygrometerDeclaration(sensorName) =>
            val newSensor = new HygrometerCheck(sensorName, referenceHum)
            (IState(Some(newSensor), ref),
              Some(sensor.classify))
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            (IState(Some(sensor), ref), 
              None)
          case Eof() =>
            (IState(None, ref),
              Some(sensor.classify))
          case Unknown(line) =>
            (IState(Some(sensor), ref),
              Some("unknown line: " + line))
        }
    }
  }
}

