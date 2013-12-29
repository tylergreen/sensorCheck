import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Input

import scalaz.concurrent.Task
import scalaz._
import scalaz.stream._


class InputSpec extends FlatSpec {
  "An Input" should "read from a file" in {
//    assert(Input.lines.length == 31)
//    assert(Input.lines(0) == "reference 70.0 45.0")
  }

  it should "take 3 elements" in {
    val nats : Process[Task, Int] = Process.range(0,100)
    assert(nats.take(3).runLog.run === List(0,1,2) )
  }

  it should "add +1 to elements" in {
    val nats : Process[Task, Int] = Process.range(0,100)
    assert(nats.take(3).map(_+1).runLog.run === List(1,2,3) )
  }

   it should "sum the stream" in {
    val nats : Process[Task, Int] = Process.range(0,100)
    assert(nats.take(4).fold1(_ + _).runLast.run === Some(6))
  }

  it should "have the fibs" in {
    assert(Input.fib.take(10).runLog.run.toList == List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34))
  }
}

