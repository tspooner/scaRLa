package scarla.domain

import scala.collection.immutable.Vector

import akka.actor.Props

trait GridWorld extends DomainSpec {

  protected val X_MIN: Int
  protected val X_MAX: Int

  protected val Y_MIN: Int
  protected val Y_MAX: Int

  val N_ACTIONS = 4 // 0-1-2-3 => Up-Right-Down-Left

  def move(pos: Tuple2[Int, Int], aid: Int): Tuple2[Int, Int] = aid match {
    case 0 => (pos._1, pos._2+1)
    case 1 => (pos._1+1, pos._2)
    case 2 => (pos._1, pos._2-1)
    case 3 => (pos._1-1, pos._2)
  }

  def availableActions(pos: Tuple2[Int, Int]): Vector[Int] = {
    var aids: List[Int] = List()

    if (pos._1 > X_MIN) aids = 3 :: aids
    if (pos._1 < X_MAX) aids = 1 :: aids

    if (pos._2 > Y_MIN) aids = 2 :: aids
    if (pos._2 < Y_MAX) aids = 0 :: aids

    aids.toVector
  }
}
