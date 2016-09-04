package scarla.domain

import scala.collection.immutable.Vector

case class State(val features: Vector[Double],
                 val isTerminal: Boolean = false) extends Serializable
