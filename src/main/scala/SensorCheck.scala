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
    referenceHum: Option[Double],
    results : List[String])

  def run(input: String) : List[String] = {

    val lines = input.split('\n').map(LineParser.parse(_))
    val initialState = State(None, None, None, None, List() : List[String])

    val State(lastSensor, lastId, _, _, resultsList : List[String]) = lines.foldLeft(initialState){ 
      case (state, line) =>
        state match {
          case s@State(None, None, None, None, results) =>
            handleFirstLine(s, line)
          case s@State(None, None, rt@Some(referenceTemp), rh@Some(referenceHum), results) =>
            handleSecondLine(s,line)
          case s@State(Some(sensor), Some(name), rt@Some(referenceTemp), rh@Some(referenceHum), results) =>
            handleRestLines(s, line)
        }
    }

    ((lastId.get + ": " + lastSensor.get.classify) :: resultsList).reverse

  }

  private def handleFirstLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, None, None, None, results) =>
        line match {
          case Reference(referenceTemperature, referenceHumidity) =>
            State(None, None, Some(referenceTemperature), Some(referenceHumidity), results)
          case _ => throw new RuntimeException("First line must be Reference")
        }
    }
  }

  private def handleSecondLine(state : State, line : InputLine) : State = {
    state match {
      case State(None, None, rt@Some(referenceTemp), rh@Some(referenceHum), results) =>
        line match {
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            State(Some(newSensor), Some(newId), rt, rh, results)
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            State(Some(newSensor), Some(newId), rt, rh, results)
          case _ => throw new RuntimeException("Second line must be declare a sensor")
        }
    }
  }

  private def handleRestLines(state: State, line: InputLine) : State = {
    state match {
      case State(Some(sensor), Some(name), rt@Some(referenceTemp), rh@Some(referenceHum), results) =>
        line match {
          case SensorDeclaration(Thermometer(newId)) =>
            val newSensor = new ThermometerCheck(referenceTemp)
            State(Some(newSensor), Some(newId), rt, rh, (name + ": " + sensor.classify) :: results)
          case SensorDeclaration(Hygrometer(newId)) =>
            val newSensor = new HygrometerCheck(referenceHum)
            State(Some(newSensor), Some(newId), rt, rh, (name + ": " + sensor.classify) :: results)
          case Reading(_,_,quantity) =>
            sensor.add(quantity)
            State(Some(sensor), Some(name), rt, rh, results)
          case Unknown(line) =>
            throw new RuntimeException("unknown line: " + line)
        }
    }
  }
}

