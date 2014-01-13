package com.tyler.sensorCheck
import sensorCheck._
import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._
import Process._

object SensorCheck {

  def main(args: Array[String]){
    println("SensorCheck running, enter input:")
    checkStream(io.stdInLines).map(_.toString).to(io.stdOutLines).run.run
  }

  def checkStream(input: Process[Task, String]) : Process[Task, Output] = {
    val parsedStream = input.map(LineParser.parse(_)).append(Process.emit(Eof()).toSource)
    val i = new Interpreter(StateTransitions.initialState, StateTransitions.nextState)
    i.transform(parsedStream)
  }

  def checkString(input: String) : List[String] = {
    checkStream(Process.emitAll(input.split('\n'))).map(Printer.print(_)).runLog.run.toList
  }

}


