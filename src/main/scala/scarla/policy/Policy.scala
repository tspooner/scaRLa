package scarla.policy

import scarla.domain.State
import scarla.mapping.Mapping
import scarla.utilities.General

import breeze.linalg.DenseVector

trait Policy {

  /**
   * π(s) => a
   */
  def pi(s: State): Int

  def terminalUpdate(s: State) = {}
}


trait DifferentiablePolicy extends Policy {

  def pi(s: State): Int =
    General.weightedSample(prob(s).iterator)


  def dlogpi(s: State, aid: Int): DenseVector[Double]

  def prob(s: State): DenseVector[Double]
  def prob(s: State, aid: Int): Double =
    prob(s)(aid)
}

trait ParameterizedPolicy extends Policy {

  val theta: DenseVector[Double]
}
