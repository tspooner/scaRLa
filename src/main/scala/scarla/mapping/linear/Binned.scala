package scarla.mapping

import breeze.linalg.DenseVector
import scarla.utilities.General.sub2ind

trait Binned extends LinearMapping {

  // Requirements:
  def discretisation: Int


  override val nFeatures: Vector[Int] =
    bounds.map(_ => discretisation)

  def featureWidths: DenseVector[Double] =
    DenseVector.tabulate(nDimensions){
      i => (bounds(i)._2 - bounds(i)._1) / nFeatures(i)
    }


  // Helpers:
  protected def _bin(v: Double, l: Double, size: Double): Int =
    ((v - l) / size).toInt

  protected def _hash(sv: Vector[Double]): Int = {
    val bs = sv.zipWithIndex.map {
      case (v, i) => _bin(v, bounds(i)._1, featureWidths(i))
    }

    sub2ind(bs, nFeatures)
  }
}
