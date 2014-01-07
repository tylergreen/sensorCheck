// import org.scalatest.FlatSpec
// import com.tyler.sensorCheck.Sensor
// import com.tyler.sensorCheck.Thermometer
// import com.tyler.sensorCheck.Hygrometer

// class SensorSpec extends FlatSpec {
//   "a Thermometer" should "classify itself" in {
//     assertResult("UltraPrecise"){
//       Thermometer("temp-0", 70, Array(("fakedate", 70),("fakedate", 70))).classify
//     }
//     assertResult("VeryPrecise"){
//       Thermometer("temp-0", 70, Array(
//         ("fakedate", 70),
//         ("fakedate", 66),
//         ("fakedate", 74))).classify
//     }

//     assertResult("Precise"){
//       Thermometer("temp-0", 70, Array(
//         ("fakedate", 0),
//         ("fakedate", 10000),
//         ("fakedate", 500))).classify
//     }

//   }

//   "A Hygrometer" should "classify itself ok if all readings are within 1% of reference" in {
//     assertResult("ok"){
//       Hygrometer("hum-1", 45, Array(("fakeDate", 44.1), ("fakeDate", 45.9))).classify
//     }
//   }

//   it should "classify itself as 'discard' if any readings are not within 1% of reference" in {
//     assertResult("discard"){
//       Hygrometer("hum-1", 45, Array(("fakeDate", 45.0), ("fakeDate", 46.0))).classify
//     }
//     assertResult("discard"){
//       Hygrometer("hum-1", 45, Array(("fakeDate", 45.0), ("fakeDate", 44.0))).classify
//     }
    
//   }


// }
