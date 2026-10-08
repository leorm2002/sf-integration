import fuse.FusedStream.*
import custom.{ToMapCollector, MinMaxCollector, minMax, toMap}

@main def hello(): Unit = {
  println("Hello world!")

  // Using a built-in collector
  val sum: Double = FusedStream
    .from(Array(1, 2, 3))
    .filter(_ % 2 == 0)
    .map(_.toDouble * 1.5)
    .collect(summing)
  println(sum)

  // Using a custom toMap collector
  val mappingResult = FusedStream
    .from(Array("apple", "banana", "cherry"))
    .collect(toMap(fruit => fruit, _.length))
  println(mappingResult)

  // Using a custom toMap collector
  val simplifiedMappingResult = FusedStream
    .from(Array("apple", "banana", "cherry"))
    .collect(toMap(_.length))
  println(simplifiedMappingResult)

  // Using a custom min-max collector
  val minMaxResult = FusedStream
    .from(Array(4, 9, 2, 7, 1))
    .collect(minMax)
  println(minMaxResult)

}
