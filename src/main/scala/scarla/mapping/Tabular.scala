package scarla.mapping

import scarla.domain.{DomainSpec, State}

import breeze.linalg.DenseVector

class Tabular(domainSpec: DomainSpec) extends LinearMapping(domainSpec) {

  def hash(sv: Vector[Double]): Int = {
    val bs = sv.zipWithIndex.map { case (v, i) => _bin(v, i) }

    bs.drop(1).zipWithIndex.foldRight(0) {
      case ((value, index), acc) => acc*nBins(index) + value
    } + bs(0)
  }


  def _phi(s: State): DenseVector[Double] = {
    val p = DenseVector.zeros[Double](dimensionality)

    p(hash(s.values)) = 1

    p
  }


  override def Q(s: State, aid: Int): Double =
    weights(hash(s.values), aid)

  override def update(s: State, aid: Int, offset: Double) =
    weights(hash(s.values), aid) += offset
}
