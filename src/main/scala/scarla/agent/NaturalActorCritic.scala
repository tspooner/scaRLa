package scarla.agent

import akka.actor.Props

import scarla.algorithm.Algorithm
import scarla.mapping.Mapping
import scarla.policy.{DifferentiablePolicy, ParameterizedPolicy, Greedy}
import scarla.domain.State

object NaturalActorCritic {

  type PT = scarla.policy.Policy with DifferentiablePolicy with ParameterizedPolicy

  def props(algo: Algorithm, mapping: Mapping, policy: PT): Props =
    Props(new NaturalActorCritic(algo, mapping, policy))
}

class NaturalActorCritic(val algo: Algorithm,
                         mapping: Mapping,
                         policy: NaturalActorCritic.PT)
  extends Agent(mapping, policy) {

  import NaturalActorCritic._

  val alpha = 0.01
  val gamma = 0.95


  def learn(s: State, aid: Int, r: Double, ns: State) = {
    val target = algo.target(ns, mapping, policy)
    val delta  = r + gamma*target - mapping.get(s, aid)

    mapping.update(s, aid, alpha*delta)
  }
}
