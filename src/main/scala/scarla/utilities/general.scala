package scarla.utilities

object General {

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
}
