import org.scalatest._

import breeze.linalg.{DenseVector, argmax}

import scarla.domain.{Fixtures => F, State}
import scarla.mapping.Tabular

class TabularSpec extends FlatSpec with Matchers {

  "A tabular mapping" should "have the correct dimensionality" in {
    val m = new Tabular(F.spec(nd=3), 20)

    m.dimensionality should be (scala.math.pow(20, 3))
  }

  it should "have no collisions in 1d" in {
    val m = new Tabular(F.spec(nd=1), 10)

    for (i <- 0 until 10) {
      val p = DenseVector.zeros[Double](10)
      p(i) = 1.0

      m._phi(Vector(i)) should be (p)
    }
  }

  it should "have no collisions in 2d" in {
    val m = new Tabular(F.spec(nd=2), 10)

    val ps = DenseVector.zeros[Double](100)
    for (i <- 0 until 10; j <- 0 until 10) {
      val l = argmax(m._phi(Vector(i, j)))

      ps(l) should be (0.0)
      ps(l) = 1.0
    }
  }

  it should "have no collisions in 3d" in {
    val m = new Tabular(F.spec(nd=3), 10)

    val ps = DenseVector.zeros[Double](10000)
    for (i <- 0 until 10; j <- 0 until 10; k <- 0 until 10) {
      val l = argmax(m._phi(Vector(i, j, k)))

      ps(l) should be (0.0)
      ps(l) = 1.0
    }
  }
}
