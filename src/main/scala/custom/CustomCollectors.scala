package custom
import fuse.FusedStream.*
import scala.collection.mutable

final class ToMapCollector[E, K, V](keyMapper: E => K,valueMapper: E => V) extends ParallelCollector[E, mutable.HashMap[K, V], Map[K, V]] {

  override inline def supplier(): mutable.HashMap[K, V] = mutable.HashMap.empty[K, V]

  override inline def accumulator(buf: mutable.HashMap[K, V],elem: E): Boolean = {
    buf.update(keyMapper(elem), valueMapper(elem))
    false
  }

  override inline def finisher(buf: mutable.HashMap[K, V]): Map[K, V] = buf.toMap

  override inline def combine(      left: mutable.HashMap[K, V],      right: mutable.HashMap[K, V]  ): mutable.HashMap[K, V] = {
    left ++= right
    left
  }
}





final class MinMaxBuffer(var min: Int,var max: Int,var initialized: Boolean)

final class MinMaxCollector extends Collector[Int, MinMaxBuffer, Option[(Int, Int)], Exhaustive] {

  inline def supplier(): MinMaxBuffer = new MinMaxBuffer(0, 0, false)

  inline def accumulator(buf: MinMaxBuffer, elem: Int): Boolean = {
    if (!buf.initialized) {
      buf.min = elem
      buf.max = elem
      buf.initialized = true
    } else {
      if (elem < buf.min) buf.min = elem
      if (elem > buf.max) buf.max = elem
    }

    false // No short circuiting
  }

  inline def finisher(buf: MinMaxBuffer): Option[(Int, Int)] =
    if (buf.initialized) Some((buf.min, buf.max)) else None
}



transparent inline def distinct[A]: A => Boolean = {
  val seen = mutable.HashSet.empty[A]
  elem => seen.add(elem)
}


transparent inline def toMap[E,K](keyMapper: E => K): ParallelCollector[E, mutable.HashMap[K, E], Map[K, E]] = new ToMapCollector[E, K, E](keyMapper, identity[E])
transparent inline def toMap[E,K,V](keyMapper: E => K,valMapper: E => V): ParallelCollector[E, mutable.HashMap[K, V], Map[K, V]] = new ToMapCollector[E, K, V](keyMapper, valMapper)
transparent inline def minMax: Collector[Int, MinMaxBuffer, Option[(Int, Int)], Exhaustive] = new MinMaxCollector
