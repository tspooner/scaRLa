package scarla.mapping

import breeze.linalg.DenseVector
import scala.collection.immutable.Vector

import scarla.domain.{DomainSpec, State}
import scarla.utilities.General.{sub2ind}

class Tabular(domainSpec: DomainSpec)
  extends LinearMapping(domainSpec) {

  val nFeatures: Vector[Int] = domainSpec.D_LIMITS.map(dl => 1+(dl._2 - dl._1).toInt)


  protected def hash(sv: Vector[Double]): Int = {
    val bs = sv.zipWithIndex.map {
      case (v, i) => (v - domainSpec.D_LIMITS(i)._1).toInt
    }

    sub2ind(bs, nFeatures)
  }


  def phi(sv: Vector[Double]): DenseVector[Double] = {
    val p = DenseVector.zeros[Double](dimensionality)
    p(hash(sv)) = 1

    p
  }


  override def Q(s: State, aid: Int): Double =
    weights(hash(s.values), aid)

  override def update(s: State, aid: Int, offset: Double) =
    weights(hash(s.values), aid) += offset
}
