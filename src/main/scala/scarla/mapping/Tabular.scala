package scarla.mapping

import scarla.domain.{DomainSpec, State}

import breeze.linalg.DenseVector

class Tabular(domainSpec: DomainSpec, discretisation: Int = 20)
  extends LinearMapping(domainSpec, discretisation) {

  def hash(sv: Vector[Double]): Int = {
    val bs = sv.zipWithIndex.map { case (v, i) => _bin(v, i) }

    bs.drop(1).zipWithIndex.foldRight(0) {
      case ((value, index), acc) => acc*nBins(index) + value
    }*nBins(0) + bs(0)
  }


  def _phi(sv: Vector[Double]): DenseVector[Double] = {
    val p = DenseVector.zeros[Double](dimensionality)

    p(hash(sv)) = 1

    p
  }


  override def Q(s: State, aid: Int): Double =
    weights(hash(s.values), aid)

  override def update(s: State, aid: Int, offset: Double) =
    weights(hash(s.values), aid) += offset
}
