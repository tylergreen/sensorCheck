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

  it should "classify 'precise' thermometers" in {
    val testInput = 
      """reference 70.0 45.0
thermometer temp-1
2007-04-05T22:00 temp-1 1000.0
2007-04-05T22:02 temp-1 70.0
2007-04-05T22:02 temp-1 20.0
"""
    assertResult(List("temp-1: precise")){
      SensorCheck.run(testInput)
    }
  }

  it should "classify 'ok' hygrometers" in {
       val testInput = 
      """reference 70.0 45.0
humidity hum-1
2007-04-05T22:04 hum-1 45.2
2007-04-05T22:05 hum-1 45.3
2007-04-05T22:06 hum-1 45.1
""" 
    assertResult(List("hum-1: OK")){
      SensorCheck.run(testInput)
    }
  }

it should "classify 'discard' hygrometers" in {
       val testInput = 
      """reference 70.0 45.0
humidity hum-2
2007-04-05T22:04 hum-2 44.4
2007-04-05T22:08 hum-2 42.1
""" 
    assertResult(List("hum-2: discard")){
      SensorCheck.run(testInput)
    }
  }


}
