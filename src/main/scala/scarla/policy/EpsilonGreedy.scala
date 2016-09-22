package scarla.policy

import scala.util.Random
import scala.collection.immutable.Set

import scarla.domain.State
import scarla.mapping.Mapping

class EpsilonGreedy(mapping: Mapping) extends Greedy(mapping) {

  val k: Double = 1.1
  var epsilon: Double = 0.01

  // override def _pi(s: State, a: Int, bestActions: Set[Int]): Double = {
    // val avgEps = epsilon / s.availableActions.size

    // if (bestActions contains a)
      // avgEps + 1 - epsilon
    // else
      // avgEps
  // }

  override def pi(s: State): Int = {
    val r = Random.nextFloat

    if (r > epsilon) super.pi(s)
    else
      s.availableActions(Random.nextInt(s.availableActions.length-1))
  }

  override def terminalUpdate(s: State) =
    epsilon /= k
}
