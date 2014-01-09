import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Hygrometer

class HygrometerSpec extends FlatSpec {
  "A Hygrometer" should "return 'ok' if all the readings are within 1% of the reference" in {
    assertResult("hum-1: OK"){
      val h = new Hygrometer("hum-1", 45)
      h.add(44.1)
      h.add(45.9)
      h.classify
    }
  }
  it should "return 'discard' if any of the readings are not within 1% of the reference" in {
    assertResult("hum-1: discard"){
      val h = new Hygrometer("hum-1", 45)
      h.add(46)
      h.classify
    }
    assertResult("hum-1: discard"){
      val h = new Hygrometer("hum-1", 45)
      h.add(45)
      h.add(45)
      h.add(44)
      h.classify
    }
  }

}
