package scarla.policy

import scala.collection.immutable.Set

import scarla.domain.State
import scarla.mapping.Mapping

class Greedy(mapping: Mapping) extends Policy(mapping) {

  // def _pi(s: State, a: Int, bestActions: Set[Int]): Double =
    // if (bestActions contains a) (1.0 / bestActions.size) else 0.0

  def pi(s: State): Int =
    mapping.Q(s).zip(s.availableActions).maxBy(_._1)._2
}
