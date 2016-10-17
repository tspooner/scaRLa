package scarla.mapping

import breeze.linalg.DenseVector

import scarla.domain.{DomainSpec, State}

class Basic(domainSpec: DomainSpec, val discretisation: Int = 20)
  extends Tabular(domainSpec) with Binned {

  override protected def hash(sv: Vector[Double]): Int = _hash(sv)
}
