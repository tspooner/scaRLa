package scarla.mapping

import java.io.File
import scarla.domain.State
import breeze.linalg.{DenseVector, DenseMatrix, csvwrite, max, *}
import scarla.utilities.General.sub2ind
import scala.collection.immutable.Vector


class Tabular(bounds: Vector[(Double, Double)], nActions: Int)
  extends LinearMapping(bounds, nActions) {

  val nFeatures: Vector[Int] = bounds.map(dl => 1+(dl._2 - dl._1).toInt)

  protected def hash(sv: Vector[Double]): Int = {
    val bs = sv.zipWithIndex.map {
      case (v, i) => (v - bounds(i)._1).toInt
    }

    sub2ind(bs, nFeatures)
  }


  def phi(sv: Vector[Double]): DenseVector[Double] = {
    val p = DenseVector.zeros[Double](dimensionality)
    p(hash(sv)) = 1

    p
  }


  override def get(s: State, aid: Int): Double =
    weights(hash(s.valueVector), aid)

  override def update(s: State, aid: Int, offset: Double) =
    weights(hash(s.valueVector), aid) += offset
}
