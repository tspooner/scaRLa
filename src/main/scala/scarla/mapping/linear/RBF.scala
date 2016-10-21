package scarla.mapping

import breeze.numerics.{pow, exp}
import breeze.linalg.{DenseVector, DenseMatrix, Axis, sum, *}

import scarla.utilities.General.{com, linspace}

class RBF(bounds: Vector[(Double, Double)], nActions: Int,
          val discretisation: Int = 6)
  extends LinearMapping(bounds, nActions) with Binned {

  val mu: DenseMatrix[Double] = {
    var c: List[List[Double]] =
      com((for (d <- (0 until nDimensions).reverse) yield
            linspace(bounds(d)._1, bounds(d)._2, discretisation)).toList)

    val cm = DenseMatrix.zeros[Double](dimensionality, nDimensions)

    for (r <- 0 until dimensionality)
      cm(r, 0 until nDimensions) := DenseVector(c(r).toArray).t

    cm
  }


  def phi(sv: Vector[Double]): DenseVector[Double] = {
    val d = mu(*, ::) - new DenseVector(sv.toArray)
    val e = sum(exp(-pow(d(*, ::) :/ featureWidths, 2.0)), Axis._1)

    e / sum(e)
  }
}
