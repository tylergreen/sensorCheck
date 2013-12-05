import org.scalatest._
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.Parser
import com.tyler.sensorCheck.Sensor
import com.tyler.sensorCheck.Thermometer
import com.tyler.sensorCheck.Hygrometer

class ParserSpec extends FlatSpec with Matchers {
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

  it should "be able to split a list into sections" in {
    val input = List("a", "1", "2", "a", "3", "4", "a", "5", "6")
    val result = List(List("a", "1", "2"), List("a", "3", "4"), List("a", "5", "6"))
    assert(result === parser.splitSections({_ == "a"}, input))

    val input2 = List(100, 1, 2, 100, 3, 4, 100, 5, 6)
    val result2 = List(List(100, 1, 2), List(100, 3, 4), List(100, 5, 6))
    assert(result2 === parser.splitSections({_ == 100}, input2))
  }

  it should "separate the readings by sensor" in {
    assertResult(4){
      parser.sensorReadings.length
    }
    
    parser.sensorReadings(0) shouldBe a [Hygrometer]
    assert{
      parser.sensorReadings(0) match {
        case Hygrometer("hum-1", _) => true
        case _ => fail
      }
     }

    assert{
      parser.sensorReadings(1) match{
        case Thermometer("temp-2", _) => true
        case _ => fail
      }
    }
  }

}
