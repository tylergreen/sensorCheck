package com.tyler.sensorCheck
import sensorCheck._

abstract class IState
case class StartState extends IState
case class NeedSensor(reference: Reference) extends IState
case class EvaluatingSensor(currentSensor : Sensor, reference: Reference) extends IState
case class SkipToNextSensor(reference: Reference) extends IState

object StateTransitions {

  val initialState = StartState()

  def nextState : (IState, InputLine) => (IState, Option[Output]) = {
    { (state : IState, line: InputLine) =>
      state match {
        case StartState() => setReference(line)
        case NeedSensor(reference) => setFirstSensor(reference, line)
        case EvaluatingSensor(sensor, reference) => continueEvaluation(reference, sensor, line)
        case SkipToNextSensor(reference) => skipToNextSensor(reference, line)
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
          Some(ErrorMsg("Error: First Line must be reference")))
    }
  }

  private def setFirstSensor(reference : Reference, line : InputLine) : (IState, Option[Output]) = {
    line match {
      case HygrometerDeclaration(sensorName) =>
        val newSensor = new Hygrometer(sensorName, reference.humidity)
        (EvaluatingSensor(newSensor, reference),
          None)
      case ThermometerDeclaration(sensorName) =>
        val newSensor = new Thermometer(sensorName, reference.temperature)
        (EvaluatingSensor(newSensor, reference),
          None)
      case _ =>
        (NeedSensor(reference),
          Some(ErrorMsg("Error: Second Line must be a sensor declaration "))
        )
    }
  }

  private def skipToNextSensor(reference: Reference, line: InputLine) : (IState, Option[Output]) = {
    line match {
      case HygrometerDeclaration(sensorName) =>
        val newSensor = new Hygrometer(sensorName, reference.humidity)
        (EvaluatingSensor(newSensor, reference),
          None)
      case ThermometerDeclaration(sensorName) =>
        val newSensor = new Thermometer(sensorName, reference.temperature)
        (EvaluatingSensor(newSensor, reference),
          None)
      case Unknown(line) =>
        (SkipToNextSensor(reference),
          Some(ErrorMsg("unknown line: " + line)))
      case _ =>
        (SkipToNextSensor(reference),
          None)
    }
  }

  private def continueEvaluation(
    reference : Reference,
    sensor : Sensor,
    line: InputLine) : (IState,Option[Output]) = {
    line match {
      case ThermometerDeclaration(sensorName) =>
        val newSensor = new Thermometer(sensorName, reference.temperature)
        (EvaluatingSensor(newSensor, reference),
          Some(Rating(sensor.name, sensor.classify)))
      case HygrometerDeclaration(sensorName) =>
        val newSensor = new Hygrometer(sensorName, reference.humidity)
        (EvaluatingSensor(newSensor, reference),
          Some(Rating(sensor.name, sensor.classify)))
      case Reading(_,_,quantity) =>
        sensor.addSample(quantity)
        (EvaluatingSensor(sensor, reference),
          None)
      case Eof() =>
        (StartState(),
          Some(Rating(sensor.name, sensor.classify)))
      case Unknown(line) =>
        (EvaluatingSensor(sensor, reference),
          Some(ErrorMsg("unknown line: " + line)))
    }
  }
}

