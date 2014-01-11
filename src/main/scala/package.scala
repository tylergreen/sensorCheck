package com.tyler.sensorCheck
package object sensorCheck {
  type TimeStamp = String
  type SensorName = String
  type ErrorMsg = String
  type Output = Either[ErrorMsg, (SensorName, SensorRating)]
}
