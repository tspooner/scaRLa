package scarla.domain

import scala.util.Random
import scala.collection.immutable.Vector

import akka.actor.Props

object CliffWalk extends GridWorld {

  // GridWorld:
  protected val X_MIN = 0
  protected val X_MAX = 9

  protected val Y_MIN = 0
  protected val Y_MAX = 4

  // DomainSpec:
  val N_DIMENSIONS = 2
  val D_LIMITS: Vector[Tuple2[Double, Double]] =
    Vector((X_MIN, X_MAX), (Y_MIN, Y_MAX))

  // Akka:
  def props: Props = Props(classOf[CliffWalk])
}

class CliffWalk extends Domain(CliffWalk) {
  import CliffWalk._

  def _randState = {
    val p = (Random.nextInt(X_MAX-1)+1, Random.nextInt(Y_MAX-2)+1)

    State(Vector(p._1, p._2), availableActions(p))
  }

  def initialState = State(Vector(0, 0), Vector(0, 1))

  def next(aid: Int): State = {
    val p  = (state.values(0).toInt, state.values(1).toInt)
    val np = move(p, aid)

    val isTerminal = (np._2 == Y_MIN) && (np._1 > X_MIN)

    State(Vector(np._1, np._2), availableActions(np), isTerminal)
  }

  def reward(s: State, ns: State): Double = ns.isTerminal match {
    case true  =>
      if (ns.values(0) == X_MAX && ns.values(1) == Y_MIN) 1.0 else -1.0

    case false =>
      -0.01
  }
}
