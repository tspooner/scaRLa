package scarla.domain

import akka.actor.Props

import scala.math

import scala.collection.immutable.Vector

object MountainCar extends DomainSpec {

  // MountainCar:
  private val X_MIN = -1.2
  private val X_MAX = 0.5

  private val V_MIN = -0.07
  private val V_MAX = 0.07

  private val STEP_REWARD = -1.0
  private val GOAL_REWARD = 0.0

  private val ACTIONS = Vector(-1, 0, 1)

  // DomainSpec:
  val N_DIMENSIONS = 2
  val D_LIMITS = Vector((X_MIN, X_MAX), (V_MIN, V_MAX))

  val N_ACTIONS = 3

  // Akka:
  def props: Props = Props(classOf[MountainCar])
}

class MountainCar extends Domain(MountainCar) {
  import MountainCar._

  def initialState =
    State(Vector(-0.5, 0.0), ALL_AIDS)

  def next(aid: Int): State = {
    val a = MountainCar.ACTIONS(aid)
    var position = state.values(0)
    var velocity = state.values(1)

    velocity += 0.001*a - 0.0025*math.cos(3*position)
    position += velocity

    if (position <= X_MIN) {
      position = X_MIN
      velocity = 0.0
    }

    if (position >= X_MAX)
      position = X_MAX

    State(Vector(position, velocity), ALL_AIDS, position == X_MAX)
  }

  def reward(s: State, ns: State): Double =
    if (ns.values(0) >= X_MAX) GOAL_REWARD else STEP_REWARD
}
