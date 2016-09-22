package scarla.policy

import scala.collection.immutable.{Seq, Set, BitSet}

import scarla.domain.State
import scarla.mapping.Mapping

abstract class Policy(val mapping: Mapping) {

  // // Abstract methods:
  /**
   * π(a | s) => Pr[a | s]
   */
  // def _pi(s: State, a: Int, bestActions: Set[Int]): Double


  // // Private methods:
  // private def _modelBest(s: State): BitSet =
    // BitSet(mapping.Q(s).zip(s.availableActions).maxBy(_._1)._2)


  // // Public methods:
  /**
   * π(a | s) => Pr[a | s]
   */
  // def pi(s: State, a: Int,
         // bestActions: Option[Set[Int]] = None): Double =
    // bestActions match {
      // case None    =>
        // _pi(s, a, _modelBest(s))

      // case _ =>
        // _pi(s, a, bestActions.get)
    // }

  /**
   * π_s(s) => { π(a | s) : a ∈ s.availableActions }
   */
  // def pi_s(s: State): Seq[Double] = {
    // val bestActions = Some(_modelBest(s))
    // s.availableActions.map(a => pi(s, a, bestActions))
  // }

  /**
   * π(s) => a
   */
  def pi(s: State): Int// =
    // mapping.Q(s).zip(s.availableActions).maxBy(_._1)._2
    // pi_s(s).zip(s.availableActions).maxBy(_._1)._2

  def terminalUpdate(s: State) = {}
}
