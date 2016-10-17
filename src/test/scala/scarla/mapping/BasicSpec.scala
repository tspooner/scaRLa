import org.scalatest._

import breeze.linalg.{DenseVector, argmax}

import scarla.domain.{Fixtures => F, State}
import scarla.mapping.Basic

class BasicSpec extends FlatSpec with Matchers {

  "A basic mapping" should "have the correct dimensionality" in {
    val m = new Basic(F.spec(nd=3), 20)

    m.dimensionality should be (scala.math.pow(20, 3))
  }

  it should "have the correct feature widths" in {
    (new Basic(F.spec(nd=1), 9)).featureWidths should be (DenseVector(1.0))
    (new Basic(F.spec(nd=1), 10)).featureWidths should be (DenseVector(0.9))
  }

  it should "have no collisions in 1d" in {
    val m = new Basic(F.spec(nd=1), 9)

    for (i <- 0 until 9) {
      val p = DenseVector.zeros[Double](9)
      p(i) = 1.0

      m.phi(Vector(i)) should be (p)
    }
  }

  it should "have no collisions in 2d" in {
    val m = new Basic(F.spec(nd=2), 9)

    val ps = DenseVector.zeros[Double](81)
    for (i <- 0 until 9; j <- 0 until 9) {
      val l = argmax(m.phi(Vector(i, j)))

      ps(l) should be (0.0)
      ps(l) = 1.0
    }
  }

  it should "have no collisions in 3d" in {
    val m = new Basic(F.spec(nd=3), 9)

    val ps = DenseVector.zeros[Double](729)
    for (i <- 0 until 9; j <- 0 until 9; k <- 0 until 9) {
      val l = argmax(m.phi(Vector(i, j, k)))

      ps(l) should be (0.0)
      ps(l) = 1.0
    }
  }
}
