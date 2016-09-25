import org.scalatest._

import breeze.linalg.{DenseVector, sum}

import scarla.domain.{Fixtures => F, State}
import scarla.mapping.RBF

class RBFSpec extends FlatSpec with Matchers {

  "An RBF mapping" should "have the correct dimensionality" in {
    val m = new RBF(F.spec(nd=3), 20)

    m.dimensionality should be (scala.math.pow(20, 3))
  }

  it should "have decreasing phi with increasing distance from the state" in {
    val m = new RBF(F.spec(nd=1), 10)
    val p = m._phi(Vector(0))

    for (i <- 1 until 10)
      p(i) should be < p(i-1)
  }

  it should "produce symmetric phi" in {
    val m = new RBF(F.spec(nd=1), 9)
    val p = m._phi(Vector(4.5))

    for (i <- 0 to 4)
      p(4+i) should be (p(4-i))
  }

  it should "normalize phi" in {
    val m = new RBF(F.spec(nd=1), 9)
    val p = m._phi(Vector(4.5))

    sum(p) should be (1.0 +- 1e-5)
  }
}
