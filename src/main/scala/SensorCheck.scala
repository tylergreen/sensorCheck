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
    sensorName : Option[String],
    reference : Option[Reference])

  def run(input: Process[Task, InputLine]) : Process[Task, String] = {

    val initialState = Process.state(State(None, None, None))

    input.zip(initialState).flatMap{ case(line, (getState, setState)) =>
      getState match {
        case s@State(None, None, None) =>
          val newState = handleFirstLine(s, line)
          eval(setState(newState)).drain
        case s@State(None, None, Some(_)) =>
          val newState = handleSecondLine(s,line)
          eval(setState(newState)).drain
        case s@State(Some(_), Some(_), Some(_)) =>
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
      case State(None, None, None) =>
        line match {
          case r@Reference(referenceTemperature, referenceHumidity) =>
            State(None, None, Some(r))
          case _ => throw new RuntimeException("First line must be Reference")
        }
    }
  }

  private def handleSecondLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, None, ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            State(Some(newSensor), Some(newId), ref)
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            State(Some(newSensor), Some(newId), ref)
          case _ => throw new RuntimeException("Second line must be declare a sensor")
        }
      }
  }

  private def handleRestLines(state: State, line: InputLine) : (State,Option[String]) = {
    state match {
      case State(Some(sensor), Some(name), ref@Some(Reference(referenceTemp, referenceHum))) =>
        line match {
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            (State(Some(newSensor), Some(newId), ref),
              Some(name + ": " + sensor.classify))
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            (State(Some(newSensor), Some(newId), ref),
              Some(name + ": " + sensor.classify))
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            (State(Some(sensor), Some(name), ref), 
              None)
          case Eof() =>
            (State(None, None, ref),
              Some(name + ": " + sensor.classify))
          case Unknown(line) =>
            (State(Some(sensor), Some(name), ref),
              Some("unknown line: " + line))
        }
    }
  }
}

