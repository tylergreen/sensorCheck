import org.scalatest.FlatSpec
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.InputLine
import com.tyler.sensorCheck.Reference
import com.tyler.sensorCheck._

class LineParserSpec extends FlatSpec {
  "LineParser" should "parse a reference line" in {
    assertResult(Reference(70.0, 45.0)){
      LineParser.parse("reference 70.0 45.0")
    }
  }

  it should "parse a thermometer declaration line" in {
    assertResult(SensorDeclaration(Thermometer("temp-1"))){
      LineParser.parse("thermometer temp-1")
    }
  }

  it should "parse a hygrometer declartion line" in {
    assertResult(SensorDeclaration(Hygrometer("hum-1"))){
      LineParser.parse("humidity hum-1")
    }
  }

  it should "parse a sensor reading line" in {
    assertResult(Reading("temp-2", "2007-04-05T22:03", 71.3)){
      LineParser.parse("2007-04-05T22:03 temp-2 71.3")
    }
  }

  it should "return error on any other line" in {
    assertResult(Unknown("dlkfjslkd")){
      LineParser.parse("dlkfjslkd")
    }
  }

}

