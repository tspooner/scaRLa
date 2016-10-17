package scarla.policy

import scarla.mapping.Mapping
import scarla.domain.State

class CustomPolicy(val mapping: Mapping, val _pi: (Mapping, State) => Int)
  extends Policy {

  def pi(s: State) = _pi(mapping, s)
}
