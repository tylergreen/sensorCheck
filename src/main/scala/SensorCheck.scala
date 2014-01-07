package com.tyler.sensorCheck
import com.tyler.sensorCheck.Input
import com.tyler.sensorCheck.LineParser
import com.tyler.sensorCheck.Sensor

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.moment.Mean

object SensorCheck {
  def run(input: String) : List[String] = {
    val lines = input.split('\n').map(LineParser.parse(_))
    val Reference(referenceTemp, referenceHum) = lines(0)
    val ThermometerDeclaration(name1) = lines(1)

    val initialState = (new ThermometerCheck(referenceTemp), name1,  List() : List[String])
    val (lastSensor, lastId, resultsList : List[String]) = lines.drop(2).foldLeft(initialState){ case ((sensor, name, results), line) =>
      line match {
        case ThermometerDeclaration(sensorId) => 
          val newSensor = new ThermometerCheck(referenceTemp)
          (newSensor, sensorId, (name + ": " + sensor.classify.format) :: results)
        case Reading(_,_,quantity) => 
          sensor.add(quantity)
          (sensor, name, results)
      }
    }

    ((lastId + ": " + lastSensor.classify.format) :: resultsList).reverse

  }

  def classifyHygrometer(name : String, referenceHum : Double, readings : Seq[Double] ) : String = {
    val tolerance = 1
    if (readings.forall(reading => tolerance >= math.abs(reading - referenceHum)))
      name + ": OK"
    else
      name + ": discard"
  }

}



