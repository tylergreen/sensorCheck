package com.tyler.sensorCheck
package object sensorCheck {
  type TimeStamp = String
  type SensorId = String
  type ErrorMsg = String
  type Output = Either[ErrorMsg, (SensorId, SensorRating)]
}
