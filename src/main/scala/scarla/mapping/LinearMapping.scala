package scarla.mapping

import scarla.domain.State
import breeze.numerics.ceil
import scala.collection.immutable.Vector
import breeze.linalg.{DenseVector, DenseMatrix, convert, max, sum, *}

abstract class LinearMapping(val bounds: Vector[(Double, Double)],
                             val nActions: Int)
  extends Mapping {

  val nDimensions: Int = bounds.size

  val nFeatures: Vector[Int]
  lazy val dimensionality: Int = nFeatures.product
  lazy val counts: DenseMatrix[Double] = _zeroMatrix
  lazy val weights: DenseMatrix[Double] = _zeroMatrix


  protected def _zeroMatrix: DenseMatrix[Double] =
    DenseMatrix.zeros[Double](dimensionality, nActions)


  def phi(sv: Vector[Double]): DenseVector[Double]

  def get(s: State, aid: Int): Double = sum(weights :* phi(s, aid))
  def update(s: State, aid: Int, offset: Double) =
    weights += phi(s, aid) * offset

  def phi(s: State, aid: Int): DenseMatrix[Double] = s.isTerminal match {
    case true  => _zeroMatrix
    case false =>
      val p = _zeroMatrix

      p(::, aid) := phi(s.valueVector)

      p
  }
}

// trait AverageMapping extends LinearMapping {

  // val counts: DenseMatrix[Double] = DenseMatrix.zeros[Double](dimensionality, nActions)

  // override def get(p: DenseMatrix[Double]): Double = {
    // val v = (weights :* p) / (counts :* p)

    // println(v)

    // sum(v)
  // }

  // override def update(p: DenseMatrix[Double], offset: Double) = {
    // counts += ceil(p)

    // super.update(p, offset)
  // }
// }
