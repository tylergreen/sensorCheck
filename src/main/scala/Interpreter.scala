package com.tyler.sensorCheck

import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._
import Process._

/**
  * This is code is based on simpler examples I obtained from the scalaz-stream source code and specs
  * using "Process.state" for embedding state into streams
  * The scalaz-stream API for this use case is a little confusing but hopefully this explains the important components 
  * 
  * getState: returns current state
  * setState: function to set the state
  * eval: evaluates an arbitrary effect in a process
  * drain: removes () emitted by eval(setState)
  * emit: emits a value to the output stream
  */

class Interpreter[S, I, O](initialState : S, nextState : (S, I) => (S, Option[O])) {

  def transform(inputStream : Process[Task, I]) : Process[Task, O] = {
    val startState = Process.state(initialState) 
    inputStream.zip(startState).flatMap { case (element, (getState, setState)) =>
      val (newState, output) = nextState(getState, element)
      output match {
        case Some(output) => eval(setState(newState)).drain ++ emit(output)
        case None => eval(setState(newState)).drain
      }
    }
  }
}
