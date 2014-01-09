import org.scalatest.FlatSpec
import com.tyler.sensorCheck.ThermometerCheck
import com.tyler.sensorCheck._

class ThermometerCheckSpec extends FlatSpec {

  "ThermometerCheck" should "return 'ultra precise' if the mean of the readings is within 0.5 degrees of the known temperature and the std deviation is < 3" in {
    assertResult("temp-1: ultra precise"){
      val check = new ThermometerCheck("temp-1", 70)
      check.add(70)
      check.add(70)
      check.classify
    }
  }

  it should "return 'very precise' if the mean is within 0.5 and the std deviation is < 5" in {
    assertResult("temp-1: very precise"){
      val check = new ThermometerCheck("temp-1", 70)
      check.add(70)
      check.add(66)
      check.add(74)
      check.classify
    }
  }

  it should "return pecise otherwise" in {
    assertResult("temp-1: precise"){
      val check = new ThermometerCheck("temp-1", 70)
      check.add(0)
      check.add(1000)
      check.add(500)
      check.classify
    }
  }
}
