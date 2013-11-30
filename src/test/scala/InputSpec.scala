import org.scalatest.FlatSpec
import com.tyler.sensorCheck.Input

class InputSpec extends FlatSpec {
  "An Input" should "read from a file" in {
    assert(Input.lines.length == 31)
    assert(Input.lines(0) == "reference 70.0 45.0")
  }
}

