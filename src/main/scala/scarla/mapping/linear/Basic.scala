package scarla.mapping

import breeze.linalg.DenseVector

class Basic(bounds: Vector[(Double, Double)], nActions: Int,
            val discretisation: Int = 10)
  extends Tabular(bounds, nActions) with Binned {

  override protected def hash(sv: Vector[Double]): Int = _hash(sv)
}
