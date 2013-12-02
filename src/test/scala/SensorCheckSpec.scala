import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Complex
import com.tyler.sensorCheck.Monkey

class SensorCheckSpec extends FlatSpec {
  "A Complex" should "have real and imaginary parts" in {
    val c1 = new Complex(20, 4)
    assert(c1.re == 20)
    assert(c1.im == 4)

  }

  it should "have more tests" in {
    val c1 = new Complex(20, 4)
    assert((c1.re + c1.im) == 24)
  }

  it should "be armed" in {
    val m = Monkey
    assert(m.arm === "monk-arm")
    m.arm = "monk-leg"
    assert(m.arm === "monk-leg")
  }


//   "A SensorCheck" should "classify vals" in {
//     val input = "reference 70.0 45.0
// thermometer temp-1
// 2007-04-05T22:00 temp-1 72.4
// 2007-04-05T22:01 temp-1 76.0
// 2007-04-05T22:02 temp-1 79.1
// 2007-04-05T22:03 temp-1 75.6
// 2007-04-05T22:04 temp-1 71.2
// 2007-04-05T22:05 temp-1 71.4
// 2007-04-05T22:06 temp-1 69.2
// 2007-04-05T22:07 temp-1 65.2
// 2007-04-05T22:08 temp-1 62.8
// 2007-04-05T22:09 temp-1 61.4
// 2007-04-05T22:10 temp-1 64.0
// 2007-04-05T22:11 temp-1 67.5
// 2007-04-05T22:12 temp-1 69.4
// thermometer temp-2
// 2007-04-05T22:01 temp-2 69.5
// 2007-04-05T22:02 temp-2 70.1
// 2007-04-05T22:03 temp-2 71.3
// 2007-04-05T22:04 temp-2 71.5
// 2007-04-05T22:05 temp-2 69.8
// humidity hum-1
// 2007-04-05T22:04 hum-1 45.2
// 2007-04-05T22:05 hum-1 45.3
// 2007-04-05T22:06 hum-1 45.1
// humidity hum-2
// 2007-04-05T22:04 hum-2 44.4
// 2007-04-05T22:05 hum-2 43.9
// 2007-04-05T22:06 hum-2 44.9
// 2007-04-05T22:07 hum-2 43.8
// 2007-04-05T22:08 hum-2 42.1"
//     val output = "temp-1: precise
// temp-2: ultra precise
// hum-1: OK
// hum-2: discard
// "


}
