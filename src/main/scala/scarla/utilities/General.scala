package scarla.utilities

object General {

  def range(n: Int): Vector[Int] = (0 until n).toVector

  def bounded(v: Double, lb: Double, ub: Double): Double =
    math.min(ub, math.max(lb, v))

  def sub2ind(sub: Vector[Int], range: Vector[Int]): Int =
    sub.dropRight(1).zipWithIndex.foldLeft(0) {
      case (acc, (value, index)) => acc*range(index) + value
    }*range.last + sub.last

  def merge[T](a: List[List[T]], b: List[T]) = a match {
    case List() => for(i <- b) yield List(i)
    case xs => for{ x <- xs; y<- b } yield y :: x
  }

  def com[T](ls: List[List[T]]) =
    ls.foldLeft(List(List[T]()))((l, x) => merge(l, x))

  def linspace(start: Double, stop: Double, length: Int) = {
    val inc = (stop - start) / (length - 1)
    List.tabulate(length)(start + _*inc)
  }

  def weightedSample[T](probs: Iterator[(T, Double)]): T = {
    def weighted(probs: Iterator[(T, Double)], rnd: Double, acc: Double = 0): T =
      probs.next match {
        case (i, p) if rnd < (acc + p) => i
        case (_, p) => weighted(probs, rnd, acc + p)
      }

    weighted(probs, scala.util.Random.nextDouble)
  }
}
