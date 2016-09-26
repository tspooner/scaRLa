package scarla.mapping

import scala.collection.immutable.Vector

import akka.actor.Actor
import akka.actor.Props

import scarla.domain.State

abstract class Mapping {

  /**
   * Q : S x A -> R
   */
  def Q(s: State, aid: Int): Double

  def update(s: State, aid: Int, offset: Double)


  // TODO: Handle multiple best actions:
  def Q(s: State): Vector[Double] =
    s.availableActions.map(a => Q(s, a))

  /**
   * V : S -> R
   */
  def V(s: State): Double =
    if (s.availableActions.length > 0) Q(s).max else 0.0
}
