* Sensor Check Program
- By Tyler Green

* How to Run

$ sbt run < someProperLogFile

for convenience, I included a test input file
$ sbt run < ./testInput.txt

* how to run tests

$ sbt test

* Caveats
- The program does necessarily print out the sensors in the same
order they were read.  This is due to scala's
groupBy method not preserving order.  This behavior could be fixed
given some development time.

- When reading from stdin, the program currently waits until it
  reaches end-of-file to before it starts processing.  
