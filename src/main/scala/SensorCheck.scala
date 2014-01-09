package com.tyler.sensorCheck

import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._

import Process._

object SensorCheck {

  def main(args: Array[String]){
    println("SensorCheck running, enter input:")
    checkStream(io.stdInLines).to(io.stdOutLines).run.run
  }

  def checkStream(input: Process[Task, String]) : Process[Task, String] = {
    val parsedStream = input.map(LineParser.parse(_)).append(Process.emit(Eof()).toSource)
    Interpreter.build(parsedStream, StateTransitions.initialState, StateTransitions.nextState)
  }
}

