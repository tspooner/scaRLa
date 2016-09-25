package scarla.policy

import scala.collection.immutable.{Seq, Set, BitSet}

import scarla.domain.State
import scarla.mapping.Mapping

trait Policy {

  /**
   * π(s) => a
   */
  def pi(s: State): Int

  def terminalUpdate(s: State) = {}
}
