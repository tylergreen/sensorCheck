package com.tyler.sensorCheck

import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._
import Process._

class Interpreter[S, I, O](initialState : S, nextState : (S, I) => (S, Option[O])) {

  def transform(input : Process[Task, I]) : Process[Task, O] = {
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
