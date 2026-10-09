import fuse.FusedStream.*
import custom.{minMax, toMap, distinct}

@main def hello(): Unit = {
  println("Hello world!")

  // Using a built-in collector
  val sum: Double = FusedStream
    .from(Array(1, 2, 3))
    .filter(_ % 2 == 0)
    .map(_.toDouble * 1.5)
    .collect(summing)
  println("Application of sum: " + sum)
  // Using a stateful filter
  val distinctResult = FusedStream
    .from(Array("apple", "banana", "banana", "cherry"))
    .filter(distinct)
    .collect(toList)
  println("Application of distinct via a stateful filter: " + distinctResult)

  // Using a custom toMap collector
  val mappingResult = FusedStream
    .from(Array("apple", "banana", "banana", "cherry"))
    .filter(distinct)
    .collect(toMap(fruit => fruit, _.length))
  println("Custom to map collector: " + mappingResult)

  // Using a custom toMap collector
  val simplifiedMappingResult = FusedStream
    .from(Array("apple", "banana", "cherry"))
    .collect(toMap(_.length))
  println("Custom to map collector: " + simplifiedMappingResult)

  // Using a custom min-max collector
  val minMaxResult = FusedStream
    .from(Array(4, 9, 2, 7, 1))
    .collect(minMax)
    .get
  println("Custom min max collector: " + minMaxResult)

}
