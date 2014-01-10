package com.tyler.sensorCheck

abstract class IState
case class StartState extends IState
case class NeedSensor(reference: Reference) extends IState
case class Processing(currentSensor : Sensor, reference: Reference) extends IState

object StateTransitions {
  type Output = String

  val initialState = StartState()

  def nextState : (IState, InputLine) => (IState, Option[Output]) = {
    { (state : IState, line: InputLine) =>
      state match {
        case StartState() => setReference(line)
        case NeedSensor(reference) => setFirstSensor(reference, line)
        case Processing(sensor, reference) => processLine(reference, sensor, line)
      }
    }
  }

  private def setReference(line : InputLine) : (IState, Option[Output]) = {
    line match {
      case r@Reference(referenceTemperature, referenceHumidity) =>
        (NeedSensor(r),
          None)
      case _ =>
        (StartState(),
          Some("Error: First Line must be reference"))
    }
  }

  private def setFirstSensor(reference : Reference, line : InputLine) : (IState, Option[Output]) = {
    line match {
      case HygrometerDeclaration(sensorName) =>
        val newSensor = new Hygrometer(sensorName, reference.humidity)
        (Processing(newSensor, reference),
          None)
      case ThermometerDeclaration(sensorName) =>
        val newSensor = new Thermometer(sensorName, reference.temperature)
        (Processing(newSensor, reference),
          None)
      case _ =>
        (NeedSensor(reference),
          Some("Error: Second Line must be a sensor declaration ")
        )
    }
  }


  private def processLine(
    reference : Reference,
    sensor : Sensor,
    line: InputLine) : (IState,Option[Output]) = {
    line match {
      case ThermometerDeclaration(sensorName) =>
        val newSensor = new Thermometer(sensorName, reference.temperature)
        (Processing(newSensor, reference),
          Some(sensor.classify))
      case HygrometerDeclaration(sensorName) =>
        val newSensor = new Hygrometer(sensorName, reference.humidity)
        (Processing(newSensor, reference),
          Some(sensor.classify))
      case Reading(_,_,quantity) =>
        sensor.add(quantity)
        (Processing(sensor, reference),
          None)
      case Eof() =>
        (StartState(),
          Some(sensor.classify))
      case Unknown(line) =>
        (Processing(sensor, reference),
          Some("unknown line: " + line))
    }
  }
}

