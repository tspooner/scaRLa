import org.scalatest._

import org.scalamock.scalatest.MockFactory

import scarla.algorithm.QLearn
import scarla.mapping.Mapping
import scarla.policy.Greedy
import scarla.domain.State

class QLearnSpec extends FlatSpec with Matchers with MockFactory {

  "The Q-learning algorithm" should "use the optimal future value" in {
    val m = mock[Mapping]
    val p = new Greedy(m)
    val s = State(Vector(0), Vector(0), false)

    (m.V _).expects(s).returning(1.0)

    val t = QLearn.target(s, m, p)
    t should be (1.0)
  }
}
