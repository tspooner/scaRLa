package scarla.domain

import scarla.utilities.General.bounded

import akka.actor.Props
import scala.collection.immutable.{Map, Vector}

object MountainCar extends DomainSpec {

  // MountainCar:
  private val STEP_REWARD = -1.0
  private val GOAL_REWARD = 0.0

  private val ACTIONS = Vector(-1, 0, 1)

  // DomainSpec:
  protected val _DEFAULT_LIMITS = Map(
    "x" -> (-1.2, 0.5),
    "v" -> (-0.07, 0.07)
  )

  val N_ACTIONS = ACTIONS.size

  // Akka:
  def props(dl: DL = Map()) = Props(new MountainCar(getLimits(dl)))
}

class MountainCar(val dLimits: MountainCar.DL) extends Domain {
  import MountainCar._

  var state = initialState
  def initialState = State(Map(
    "x" -> -0.5, "v" -> 0.0
  ), ALL_AIDS)

  def next(aid: Int): State = {
    val a = ACTIONS(aid)
    var x = state.values("x")
    var v = state.values("v")

    v = bounded(v + 0.001*a - 0.0025*scala.math.cos(3*x),
                dLimits("v")._1, dLimits("v")._2)
    x = bounded(x + v, dLimits("x")._1, dLimits("x")._2)

    if (x <= dLimits("x")._1)
      v = 0.0

    State(Map("x" -> x, "v" -> v), ALL_AIDS, x == dLimits("x")._2)
  }

  def reward(s: State, ns: State): Double =
    if (ns.values("x") >= dLimits("x")._2) GOAL_REWARD else STEP_REWARD
}
