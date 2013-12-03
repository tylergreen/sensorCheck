import org.scalatest.FlatSpec
import com.tyler.sensorCheck.HygrometerCheck

class HygrometerCheckSpec extends FlatSpec {
  "HygrometerCheck" should "return 'ok' if all the readings are within 1% of the reference" in {
    assertResult("ok"){
      HygrometerCheck.classify(45.0, Array(44.1, 45.9))
    }
  }
  it should "return 'discard' if any of the readings are not within 1% of the reference" in {
    assertResult("discard"){
      HygrometerCheck.classify(45.0, Array(46))
    }
    assertResult("discard"){
      HygrometerCheck.classify(45.0, Array(45.0, 45.0, 44.0))
    }
  }

}
