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
    referenceTemp: Option[Double],
    referenceHum: Option[Double])

  def run(input: String) : List[String] = {

    val lines = Process.emitAll(input.split('\n').map(LineParser.parse(_))).toSource ++ Process.constant(Eof()).take(1)

    val initialState = Process.state(State(None, None, None, None))

    lines.zip(initialState).flatMap{ case(line, (getState, setState)) =>
      getState match {
        case s@State(None, None, None, None) =>
          val newState = handleFirstLine(s, line)
          eval(setState(newState)).drain
        case s@State(None, None, rt@Some(referenceTemp), rh@Some(referenceHum)) =>
          val newState = handleSecondLine(s,line)
          eval(setState(newState)).drain
        case s@State(Some(sensor), Some(name), rt@Some(referenceTemp), rh@Some(referenceHum)) =>
          val (newState, output) = handleRestLines(s, line)
            output match {
            case Some(result) => eval(setState(newState)).drain ++ emit(result)
            case None => eval(setState(newState)).drain
          }
      }
    }.runLog.run.toList
  }


  private def handleFirstLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, None, None, None) =>
        line match {
          case Reference(referenceTemperature, referenceHumidity) =>
            State(None, None, Some(referenceTemperature), Some(referenceHumidity))
          case _ => throw new RuntimeException("First line must be Reference")
        }
    }
  }

  private def handleSecondLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, None, rt@Some(referenceTemp), rh@Some(referenceHum)) =>
        line match {
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            State(Some(newSensor), Some(newId), rt, rh)
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            State(Some(newSensor), Some(newId), rt, rh)
          case _ => throw new RuntimeException("Second line must be declare a sensor")
        }
    }
  }

  private def handleRestLines(state: State, line: InputLine) : (State,Option[String]) = {
    state match {
      case State(Some(sensor), Some(name), rt@Some(referenceTemp), rh@Some(referenceHum)) =>
        line match {
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            (State(Some(newSensor), Some(newId), rt, rh),
              Some(name + ": " + sensor.classify))
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            (State(Some(newSensor), Some(newId), rt, rh),
              Some(name + ": " + sensor.classify))
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            (State(Some(sensor), Some(name), rt, rh), None)
          case Eof() =>
            (State(None, None, rt, rh),
              Some(name + ": " + sensor.classify))
          case Unknown(line) =>
            (State(Some(sensor), Some(name), rt, rh),
              Some("unknown line: " + line))
        }
    }
  }
}

