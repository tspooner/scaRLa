package scarla.algorithm

import scarla.domain.State
import scarla.policy.Policy
import scarla.mapping.Mapping

trait Algorithm {
  def target(s: State, mapping: Mapping, policy: Policy): Double
}
