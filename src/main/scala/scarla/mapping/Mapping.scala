package scarla.mapping

import scarla.domain.State
import breeze.linalg.{DenseVector, max}
import scala.collection.immutable.Vector

abstract class Mapping {

  def get(s: State, aid: Int): Double
  def get(s: State): Vector[Double] =
    s.availableActions.map(a => get(s, a))

  // TODO: Handle multiple maxima...
  def max(s: State): Double =
    if (s.availableActions.length > 0) get(s).max else 0.0

  def update(s: State, aid: Int, offset: Double)
}
