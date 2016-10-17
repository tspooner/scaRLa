package scarla.mapping

import breeze.linalg.{DenseVector, DenseMatrix, Axis, argmax, sum, *}
import breeze.numerics.{pow, exp}

import scarla.domain.{DomainSpec, State}
import scarla.utilities.General.{com, linspace}

class RBF(domainSpec: DomainSpec, val discretisation: Int = 6)
  extends LinearMapping(domainSpec) with Binned {


  val mu: DenseMatrix[Double] = {
    var c: List[List[Double]] =
      com((for (d <- (0 until domainSpec.N_DIMENSIONS).reverse) yield
            linspace(domainSpec.D_LIMITS(d)._1,
                     domainSpec.D_LIMITS(d)._2,
                     discretisation)).toList)

    val cm = DenseMatrix.zeros[Double](dimensionality, domainSpec.N_DIMENSIONS)

    for (r <- 0 until dimensionality)
      cm(r, 0 until domainSpec.N_DIMENSIONS) := DenseVector(c(r).toArray).t

    cm
  }


  def phi(sv: Vector[Double]): DenseVector[Double] = {
    val d = mu(*, ::) - new DenseVector(sv.toArray)
    val e = sum(exp(-pow(d(*, ::) :/ featureWidths, 2.0)), Axis._1)

    e / sum(e)
  }
}
