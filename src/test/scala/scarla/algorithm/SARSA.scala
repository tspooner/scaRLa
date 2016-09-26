import org.scalatest._

import org.scalamock.scalatest.MockFactory

import scarla.algorithm.SARSA
import scarla.mapping.Mapping
import scarla.policy.Greedy
import scarla.domain.State

class SARSASpec extends FlatSpec with Matchers with MockFactory {

  "The SARSA algorithm" should "use the on-policy future value" in {
    val m = mock[Mapping]
    val p = new Greedy(m)
    val s = State(Vector(0), Vector(0, 1), false)

    (m.Q(_: State)).expects(s).returning(Vector(2.0, -1.0))
    (m.Q(_: State, _: Int)).expects(s, 0).returning(2.0)

    val t = SARSA.target(s, m, p)
    t should be (2.0)
  }
}
