import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.Parser


class ParserSpec extends FlatSpec {
  val parser = new Parser(Input.lines)
  
  "A Parser" should "know the reference humidity" in {
    assertResult(45.0){
      parser.referenceHumidity
    }
  }

  it should "know the reference temperature" in {
    assertResult(70.0){
      parser.referenceTemperature
    }

  }

}
