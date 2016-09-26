package scarla.algorithm

import scarla.domain.State
import scarla.policy.Policy
import scarla.mapping.Mapping

object SARSA extends Algorithm {
  def target(s: State, mapping: Mapping, policy: Policy): Double =
    mapping.Q(s, policy.pi(s))
}
