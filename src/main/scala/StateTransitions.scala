package com.tyler.sensorCheck
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

case class MyState(
  currentSensor : Option[Sensor],
  reference : Option[Reference])

object StateTransitions {

  val initialState = MyState(None, None)

  def nextState : (MyState, InputLine) => (MyState, Option[String]) = {
    { (state : MyState, line: InputLine) =>
      state match {
        case s@MyState(None, None) => setReference(s, line)
        case s@MyState(None, Some(_)) => setFirstSensor(s,line)
        case s@MyState(Some(_), Some(_)) => handleRestLines(s, line)
      }
    }
  }

  private def setReference(state : MyState, line : InputLine) : (MyState, Option[String]) = {
    state match {
      case MyState(None, None) =>
        line match {
          case r@Reference(referenceTemperature, referenceHumidity) =>
            (MyState(None, Some(r)),
              None)
          case _ =>
            (MyState(None, None),
              Some("Error: First Line must be reference"))
          }
    }
  }

  private def setFirstSensor(state : MyState, line : InputLine) : (MyState, Option[String]) = {
    state match {
      case MyState(None, ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case HygrometerDeclaration(sensorName) =>
            val newSensor = new HygrometerCheck(sensorName, referenceHum)
            (MyState(Some(newSensor), ref),
              None)
          case ThermometerDeclaration(sensorName) =>
            val newSensor = new ThermometerCheck(sensorName, referenceTemp)
            (MyState(Some(newSensor), ref),
              None)
          case _ =>
            (MyState(None, ref),
              Some("Error: Second Line must be a sensor declaration ")
            )
        }
    }
  }

  private def handleRestLines(state: MyState, line: InputLine) : (MyState,Option[String]) = {
    state match {
      case MyState(Some(sensor), ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case ThermometerDeclaration(sensorName) =>
            val newSensor = new ThermometerCheck(sensorName, referenceTemp)
            (MyState(Some(newSensor), ref),
              Some(sensor.name + ": " + sensor.classify))
          case HygrometerDeclaration(sensorName) =>
            val newSensor = new HygrometerCheck(sensorName, referenceHum)
            (MyState(Some(newSensor), ref),
              Some(sensor.name + ": " + sensor.classify))
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            (MyState(Some(sensor), ref), 
              None)
          case Eof() =>
            (MyState(None, ref),
              Some(sensor.name + ": " + sensor.classify))
          case Unknown(line) =>
            (MyState(Some(sensor), ref),
              Some("unknown line: " + line))
        }
    }
  }
}

