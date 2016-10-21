package scarla.policy

import scarla.domain.State
import scarla.mapping.LinearMapping

import breeze.linalg.DenseVector

class Gibbs(val mapping: LinearMapping)
  extends ParameterizedPolicy with DifferentiablePolicy {

  val theta: DenseVector[Double] = DenseVector.zeros(10)


  def dlogpi(s: State, aid: Int): DenseVector[Double] =
    DenseVector.zeros(mapping.dimensionality)

  def prob(s: State): DenseVector[Double] = {
    val eQs = mapping.get(s).map(scala.math.exp)
    val norm = eQs.sum

    return DenseVector.tabulate(s.availableActions.size){
      i => eQs(i) / norm
    }
  }
}
