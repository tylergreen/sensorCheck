import org.scalatest.FlatSpec
import com.tyler.sensorCheck.ThermometerCheck

class ThermometerCheckSpec extends FlatSpec {

  "ThermometerCheck" should "return 'ultra precise' if the mean of the readings is within 0.5 degrees of the known temperature and the std deviation is < 3" in {
    assertResult("UltraPrecise"){
      ThermometerCheck.classify(70.0, Array(70, 70, 70))
    }

  }

  it should "return 'very precise' if the mean is within 0.5 and the std deviation is < 5" in {
    assertResult("VeryPrecise"){
      ThermometerCheck.classify(70.0, Array(70, 66, 74))
    }
  }

  it should "return pecise otherwise" in {
    assertResult("Precise"){
      ThermometerCheck.classify(70.0, Array(0, 10000, 500))
    }
  }
}
