package com.tyler.sensorCheck
import scala.util.control.Exception._
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._

import Process._

object SensorCheck {
  case class State(
    currentSensor : Option[Sensor],
    reference : Option[Reference])

  def run(input: Process[Task, InputLine]) : Process[Task, String] = {

    val initialState = Process.state(State(None, None))

    input.zip(initialState).flatMap{ case(line, (getState, setState)) =>
      getState match {
        case s@State(None, None) =>
          val newState = handleFirstLine(s, line)
          eval(setState(newState)).drain
        case s@State(None, Some(_)) =>
          val newState = handleSecondLine(s,line)
          eval(setState(newState)).drain
        case s@State(Some(_), Some(_)) =>
          val (newState, output) = handleRestLines(s, line)
            output match {
            case Some(result) => eval(setState(newState)).drain ++ emit(result)
            case None => eval(setState(newState)).drain
          }
      }
    }
  }

  private def handleFirstLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, None) =>
        line match {
          case r@Reference(referenceTemperature, referenceHumidity) =>
            State(None, Some(r))
          case _ => throw new RuntimeException("First line must be Reference")
        }
    }
  }

  private def handleSecondLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case SensorDeclaration(Hygrometer(sensorName)) =>
            val newSensor = new HygrometerCheck(sensorName, referenceHum)
            State(Some(newSensor), ref)
          case SensorDeclaration(Thermometer(sensorName)) =>
            val newSensor = new ThermometerCheck(sensorName, referenceTemp)
            State(Some(newSensor), ref)
          case _ => throw new RuntimeException("Second line must be declare a sensor")
        }
      }
  }

  private def handleRestLines(state: State, line: InputLine) : (State,Option[String]) = {
    state match {
      case State(Some(sensor), ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case SensorDeclaration(Thermometer(sensorName)) =>
            val newSensor = new ThermometerCheck(sensorName, referenceTemp)
            (State(Some(newSensor), ref),
              Some(sensor.name + ": " + sensor.classify))
          case SensorDeclaration(Hygrometer(sensorName)) =>
            val newSensor = new HygrometerCheck(sensorName, referenceHum)
            (State(Some(newSensor), ref),
              Some(sensor.name + ": " + sensor.classify))
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            (State(Some(sensor), ref), 
              None)
          case Eof() =>
            (State(None, ref),
              Some(sensor.name + ": " + sensor.classify))
          case Unknown(line) =>
            (State(Some(sensor), ref),
              Some("unknown line: " + line))
        }
    }
  }
}

