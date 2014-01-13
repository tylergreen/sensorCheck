* Sensor Check Program
- By Tyler Green

* How to Run

$ sbt run < someProperLogFile

for convenience, I included a test input file
$ sbt run < ./testInput.txt

* how to run tests

$ sbt test

* Project Overview

- SensorCheck := high-level interface
- LineParser := parser utilizing scala's parser combinators
- StateTransitions := the set of states and state transisitions
- Interpreter := low-level, scalaz-stream style processor that drives the state transitions
- Printer := converts output data to strings
- Hygrometer := models hygrometer (humidity sensor) evaluation
- Thermometer := models thermometer evaluation

* FAQ

What is different in version 2?


