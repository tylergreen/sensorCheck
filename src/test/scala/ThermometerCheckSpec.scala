import org.scalatest.FlatSpec
import com.tyler.sensorCheck.ThermometerCheck
import com.tyler.sensorCheck._

class ThermometerCheckSpec extends FlatSpec {

  "ThermometerCheck" should "return 'ultra precise' if the mean of the readings is within 0.5 degrees of the known temperature and the std deviation is < 3" in {
    assertResult(UltraPrecise){
      val check = new ThermometerCheck(70)
      check.add(70)
      check.add(70)
      check.classify
    }
  }

  it should "return 'very precise' if the mean is within 0.5 and the std deviation is < 5" in {
    assertResult(VeryPrecise){
      val check = new ThermometerCheck(70)
      check.add(70)
      check.add(66)
      check.add(74)
      check.classify
    }
  }

  it should "return pecise otherwise" in {
    assertResult(Precise){
      val check = new ThermometerCheck(70)
      check.add(0)
      check.add(1000)
      check.add(500)
      check.classify
    }
  }
}
