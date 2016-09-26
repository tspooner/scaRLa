package scarla.agent

import akka.actor.Props

import scarla.algorithm.Algorithm
import scarla.mapping.Mapping
import scarla.policy.Policy
import scarla.domain.State

object TDControlAgent {

  def props(algo: Algorithm, mapping: Mapping, policy: Policy): Props =
    Props(new TDControlAgent(algo, mapping, policy))
}

class TDControlAgent(val algo: Algorithm, mapping: Mapping, policy: Policy)
  extends Agent(mapping, policy) {

  import TDControlAgent._

  val alpha = 0.005
  val gamma = 0.95


  def learn(s: State, aid: Int, r: Double, ns: State) = {
    val target = algo.target(ns, mapping, policy)
    val delta  = r + gamma*target - mapping.Q(s, aid)

    mapping.update(s, aid, alpha*delta)
  }
}
