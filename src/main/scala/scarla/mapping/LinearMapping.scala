package scarla.mapping

import breeze.linalg.{DenseVector, DenseMatrix, Axis, sum, *}

import scarla.domain.{State, DomainSpec}

abstract class LinearMapping(val domainSpec: DomainSpec,
                             val discretisation: Int = 20) extends Mapping {

  val nBins: Vector[Int] = domainSpec.D_LIMITS.map(_ => discretisation)
  val binWidths: DenseVector[Double] =
    DenseVector.tabulate(domainSpec.N_DIMENSIONS){
      i => (domainSpec.D_LIMITS(i)._2 - domainSpec.D_LIMITS(i)._1)/nBins(i).toDouble
    }

  val dimensionality: Int = nBins.product

  val weights: DenseMatrix[Double] = _zeroMatrix


  protected def _bin(value: Double, index: Int): Int = {
    val b = ((value - domainSpec.D_LIMITS(index)._1) / binWidths(index)).toInt

    if (b == nBins(index)) b-1 else b
  }

  protected def _zeroMatrix: DenseMatrix[Double] =
    DenseMatrix.zeros[Double](dimensionality, domainSpec.N_ACTIONS)

  protected def _phi(s: State): DenseVector[Double]


  // TODO: Base logic for Q(s) is inefficient, override here to take advantage
  //       of the phi(s) method, otherwise we calculate the same values for 
  //       phi `N_ACTIONS` number of times on each step.
  // override def Q(s: State)

  def Q(s: State, aid: Int): Double =
    sum(weights :* phi(s, aid))

  def update(s: State, aid: Int, offset: Double) =
    weights += phi(s, aid) * offset

  def update(offsets: DenseMatrix[Double]) =
    weights :+= offsets


  // def phi(s: State): DenseMatrix[Double] = s.isTerminal match {
    // case true  => _zeroMatrix
    // case false =>
      // val p = _zeroMatrix

      // p(::, *) := _phi(s)

      // p
  // }

  def phi(s: State, aid: Int): DenseMatrix[Double] = s.isTerminal match {
    case true  => _zeroMatrix
    case false =>
      val p = _zeroMatrix

      p(::, aid) := _phi(s)

      p
  }
}
