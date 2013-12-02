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

  it should "be able to split a list into sections" in {
    val input = List("a", "1", "2", "a", "3", "4", "a", "5", "6")
    val result = List(List("a", "1", "2"), List("a", "3", "4"), List("a", "5", "6"))
    assert(result === parser.splitSections({_ == "a"}, input))

    val input2 = List(100, 1, 2, 100, 3, 4, 100, 5, 6)
    val result2 = List(List(100, 1, 2), List(100, 3, 4), List(100, 5, 6))
    assert(result2 === parser.splitSections({_ == 100}, input2))
  }


  // it should "separate the readings by sensor" in {
  //   assertResult(4){
  //     parser.sensorReadings.length
  //   }
  //   assertResult("thermometer temp-1") {
  //     parser.sensorReadings(0)(0)
  //   }
  //   assertResult("thermometer temp-2") {
  //     parser.sensorReadings(1)(0)
  //   }
  //   assertResult("thermometer temp-2") {
  //     parser.sensorReadings(2)(0)
  //   }
  //   assertResult("humidity hum-2") {
  //     parser.sensorReadings(3)(0)
  //   }
  // }

}
