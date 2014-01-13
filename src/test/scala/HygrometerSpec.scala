import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Hygrometer
import com.tyler.sensorCheck.Ok
import com.tyler.sensorCheck.Discard

class HygrometerSpec extends FlatSpec {
  "A Hygrometer" should "return 'ok' if all the readings are within 1% of the reference" in {
    assertResult(Ok()){
      val h = new Hygrometer("hum-1", 45)
      h.addSample(44.1)
      h.addSample(45.9)
      h.classify
    }
  }
  it should "return 'discard' if any of the readings are not within 1% of the reference" in {
    assertResult(Discard()){
      val h = new Hygrometer("hum-1", 45)
      h.addSample(46)
      h.classify
    }
    assertResult(Discard()){
      val h = new Hygrometer("hum-1", 45)
      h.addSample(45)
      h.addSample(45)
      h.addSample(44)
      h.classify
    }
  }

}
