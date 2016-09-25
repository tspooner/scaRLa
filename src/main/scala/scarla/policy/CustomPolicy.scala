package scarla.policy

import scarla.mapping.Mapping
import scarla.domain.State

class CustomPolicy(mapping: Mapping, val _pi: (Mapping, State) => Int)
  extends AdaptivePolicy(mapping) {

  def pi(s: State) = _pi(mapping, s)
}
