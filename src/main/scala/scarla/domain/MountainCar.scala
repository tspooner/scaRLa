package scarla.domain

import akka.actor.Props

import scala.math
import scala.collection.immutable.Vector

object MountainCar {

  val X_MIN, X_MAX = (-1.2, 0.5)      // Lower/Upper bounds on position
  val V_MIN, V_MAX = (-0.07, 0.07)    // Lower/Upper bounds on velocity

  val STEP_REWARD = -1
  val GOAL_REWARD = 0

  val GOAL = .5

  val ACTIONS = Vector(-1, 0, 1)


  def props: Props = Props(classOf[MountainCar])
}

class MountainCar extends Domain {

  def initialState: State =
    State(Vector(-0.5, 0.0))

  def doAction(aid: Int) = {
    val a = MountainCar.ACTIONS(aid)
    var position = state.features(0)
    var velocity = state.features(1)

    velocity += 0.001*a - 0.0025*math.cos(3*position)
    position += velocity

    state = State(Vector(position, velocity), true)
  }
}
