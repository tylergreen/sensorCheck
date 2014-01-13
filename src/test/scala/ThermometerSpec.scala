import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Thermometer
import com.tyler.sensorCheck._

class ThermometerSpec extends FlatSpec {

  "A Thermometer" should "return 'ultra precise' if the mean of the readings is within 0.5 degrees of the known temperature and the std deviation is < 3" in {
    assertResult(UltraPrecise()){
      val check = new Thermometer("temp-1", 70)
      check.addSample(70)
      check.addSample(70)
      check.classify
    }
  }

  it should "return 'very precise' if the mean is within 0.5 and the std deviation is < 5" in {
    assertResult(VeryPrecise()){
      val check = new Thermometer("temp-1", 70)
      check.addSample(70)
      check.addSample(66)
      check.addSample(74)
      check.classify
    }
  }

  it should "return pecise otherwise" in {
    assertResult(Precise()){
      val check = new Thermometer("temp-1", 70)
      check.addSample(0)
      check.addSample(1000)
      check.addSample(500)
      check.classify
    }
  }
}
