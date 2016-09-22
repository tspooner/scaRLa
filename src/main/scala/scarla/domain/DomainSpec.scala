package scarla.domain

import scala.collection.immutable.Vector

trait DomainSpec {

  val N_DIMENSIONS: Int
  val D_LIMITS: Vector[Tuple2[Double, Double]]

  val N_ACTIONS: Int

  def ALL_AIDS: Vector[Int] =
    (0 until N_ACTIONS).toVector
}
