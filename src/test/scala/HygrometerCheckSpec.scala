import org.scalatest.FlatSpec
import com.tyler.sensorCheck.HygrometerCheck
import com.tyler.sensorCheck.Ok
import com.tyler.sensorCheck.Discard

class HygrometerCheckSpec extends FlatSpec {
  "HygrometerCheck" should "return 'ok' if all the readings are within 1% of the reference" in {
    assertResult(Ok){
      val h = new HygrometerCheck(45)
      h.add(44.1)
      h.add(45.9)
      h.classify
    }
  }
  it should "return 'discard' if any of the readings are not within 1% of the reference" in {
    assertResult(Discard){
      val h = new HygrometerCheck(45)
      h.add(46)
      h.classify
    }
    assertResult(Discard){
      val h = new HygrometerCheck(45)
      h.add(45)
      h.add(45)
      h.add(44)
      h.classify
    }
  }

}
