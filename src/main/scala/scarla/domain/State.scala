package scarla.domain

import scala.collection.immutable.Vector

object State {
  final case class Transition(s: State, aid: Int, r: Double, ns: State)
}

case class State(val values: Map[String, Double],
                 val availableActions: Vector[Int],
                 val isTerminal: Boolean = false) extends Serializable {

  def valueVector = values.values.toVector

  def terminated = new State(values, Vector(), true)
}
