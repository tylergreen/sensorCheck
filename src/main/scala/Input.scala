package com.tyler.sensorCheck
import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._

import Process._

object Input {
  val lines = Array("")

  val converter: Task[Unit] = 
    io.linesStream(System.in).
      map(s => "you said: " + s).
      to(io.stdOutLines).
      run

  val pState = Process.state(0)
  val prompt: Task[Unit] =
    io.linesStream(System.in).
      map(s => s.toInt).
      zip(pState).
      flatMap{ case (line, (get, set)) =>
        if (line == 66)
          halt
        else if (line % 2 == 0)
          eval(set(get + line)).drain
        else
          emit((get + line).toString)
      }.to(io.stdOutLines).run



  val s = Process.state((0, 1))
  val fib = Process(0, 1) ++ s.flatMap { case (get, set) =>
    val (prev0, prev1) = get
    val next = prev0 + prev1
    eval(set((prev0 + 1, prev1 + 1))).drain ++ emit(next)
    //eval(set((prev1, next))).drain ++ emit(next)
    //
  }


  val l = fib.take(10).runLog.run.toList
  // val summer: Task[Int] = 
  //   io.linesStream(System.in).
  //     map(s => s.toDouble).
  //     fold(0)(_ + _).

  //     run

  val myrange : Process[Task, Int] = Process.range(0,3)
}

