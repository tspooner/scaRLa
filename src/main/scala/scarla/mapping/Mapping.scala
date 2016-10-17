package scarla.mapping

import akka.actor.{Actor, Props}
import scala.collection.immutable.Vector
import breeze.linalg.{DenseVector, DenseMatrix, Axis, sum, *}

import scarla.domain.{DomainSpec, State}

abstract class Mapping {

  /**
   * Q : S x A -> R
   */
  def Q(s: State, aid: Int): Double

  def update(s: State, offset: Double)
  def update(s: State, aid: Int, offset: Double)


  // TODO: Handle multiple best actions:
  def Q(s: State): Vector[Double] =
    s.availableActions.map(a => Q(s, a))

  /**
   * V : S -> R
   */
  def V(s: State): Double =
    if (s.availableActions.length > 0) Q(s).max else 0.0
}


abstract class LinearMapping(val domainSpec: DomainSpec) extends Mapping {

  val nFeatures: Vector[Int]
  lazy val dimensionality: Int = nFeatures.product
  lazy val weights: DenseMatrix[Double] = _zeroMatrix


  protected def _zeroMatrix: DenseMatrix[Double] =
    DenseMatrix.zeros[Double](dimensionality, domainSpec.N_ACTIONS)


  def phi(sv: Vector[Double]): DenseVector[Double]


  // TODO: Base logic for Q(s) is inefficient, override here to take advantage
  //       of the phi(s) method, otherwise we calculate the same values for 
  //       phi `N_ACTIONS` number of times on each step.
  // override def Q(s: State)

  def Q(s: State, aid: Int): Double =
    sum(weights :* phi(s, aid))

  def update(s: State, offset: Double) =
    weights(::, *) += phi(s.values) * offset

  def update(s: State, aid: Int, offset: Double) =
    weights += phi(s, aid) * offset


  def phi(s: State, aid: Int): DenseMatrix[Double] = s.isTerminal match {
    case true  => _zeroMatrix
    case false =>
      val p = _zeroMatrix

      p(::, aid) := phi(s.values)

      p
  }
}
