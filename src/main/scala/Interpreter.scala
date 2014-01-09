package com.tyler.sensorCheck

import com.tyler.sensorCheck.StateTransitions
import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._

import Process._

object Interpreter {
  def build(input : Process[Task, InputLine],
    initialState : IState,
    nextState : (IState, InputLine) => (IState, Option[String])) : Process[Task, String] = {

    val s0 = Process.state(initialState)
    input.zip(s0).flatMap { case (inputLine, (getState, setState)) =>
      val (newState, output) = nextState(getState, inputLine)
      output match {
        case Some(output) => eval(setState(newState)).drain ++ emit(output)
        case None => eval(setState(newState)).drain
      }
    }
  }
}