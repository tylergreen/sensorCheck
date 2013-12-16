package com.tyler.sensorCheck
import scalaz.stream._
import scalaz.concurrent.Task

object Input {
  val converter: Task[Unit] = 
    io.linesStream(System.in).
      map(s => "you said: " + s).
      to(io.stdOutLines).
      run

  val summer: Task[Unit] = 
    io.linesStream(System.in).
      map(s => s.toDouble).
      fold(0)(_ + _).
      .map
      to(io.stdOutLines).
      run

}
