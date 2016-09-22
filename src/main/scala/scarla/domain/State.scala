package scarla.domain

import scala.collection.immutable.Vector

object State {
  final case class Transition(s: State, aid: Int, r: Double, ns: State)
}

case class State(val values: Vector[Double],
                 val availableActions: Vector[Int],
                 val isTerminal: Boolean = false) extends Serializable
