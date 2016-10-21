package scarla.domain

import scala.util.Random
import scala.collection.immutable.{Map, Vector}

import akka.actor.Props

trait GridWorld extends DomainSpec {

  // DomainSpec:
  val N_ACTIONS = 4 // 0-1-2-3 => Up-Right-Down-Left

  // GridWorld:
  def move(pos: Tuple2[Int, Int], aid: Int): Tuple2[Int, Int] = aid match {
    case 0 => (pos._1, pos._2+1)
    case 1 => (pos._1+1, pos._2)
    case 2 => (pos._1, pos._2-1)
    case 3 => (pos._1-1, pos._2)
  }

  def availableActions(pos: Tuple2[Int, Int], dLimits: DL): Vector[Int] = {
    var aids: List[Int] = List()

    if (pos._1 > dLimits("x")._1) aids = 3 :: aids
    if (pos._1 < dLimits("x")._2) aids = 1 :: aids

    if (pos._2 > dLimits("y")._1) aids = 2 :: aids
    if (pos._2 < dLimits("y")._2) aids = 0 :: aids

    aids.toVector
  }
}


object CliffWalk extends GridWorld {

  // DomainSpec:
  protected val _DEFAULT_LIMITS: DL = Map(
    "x" -> (0, 9),
    "y" -> (0, 4)
  )

  // Akka:
  def props(dl: DL = Map()) = Props(new CliffWalk(getLimits(dl)))
}

class CliffWalk(val dLimits: CliffWalk.DL) extends Domain {
  import CliffWalk._

  def _randState = {
    val nx = Random.nextInt(dLimits("x")._2.toInt-1)+1
    val ny = Random.nextInt(dLimits("y")._2.toInt-2)+1

    State(Map("x" -> nx, "y" -> ny), availableActions((nx, ny), dLimits))
  }

  var state = initialState
  def initialState = State(Map("x" -> 0.0, "y" -> 0.0), Vector(0, 1))

  def next(aid: Int): State = {
    val p  = (state.values("x").toInt, state.values("y").toInt)
    val (nx, ny) = move(p, aid)

    val isTerminal = (ny == dLimits("y")._1) && (nx > dLimits("x")._1)

    State(Map("x" -> nx, "y" -> ny),
          availableActions((nx, ny), dLimits),
          isTerminal)
  }

  def reward(s: State, ns: State): Double = ns.isTerminal match {
    case true  =>
      if (ns.values("x") == dLimits("x")._2 &&
          ns.values("y") == dLimits("y")._2) 1.0 else -1.0

    case false =>
      -0.01
  }
}
