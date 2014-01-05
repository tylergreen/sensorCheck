import org.scalatest.FlatSpec
import com.tyler.sensorCheck.SensorCheck

class SensorCheckSpec extends FlatSpec {
  "SensorCheck" should "classify 'ultra precise' thermometers" in {
    val testInput = 
      """reference 70.0 45.0
thermometer temp-1
2007-04-05T22:00 temp-1 70.0
2007-04-05T22:01 temp-1 70.0
"""
    assertResult(List("temp-1: ultra precise")){
      SensorCheck.run(testInput)
    }
  }

  it should "classify 'very precise' thermometers" in {
    val testInput =
      """reference 70.0 45.0
thermometer temp-1
2007-04-05T22:00 temp-1 70.0
2007-04-05T22:01 temp-1 66.0
2007-04-05T22:02 temp-1 74.0
"""
    assertResult(List("temp-1: very precise")){
      SensorCheck.run(testInput)
    }
  }
}
