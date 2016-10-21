package scarla.domain

import akka.actor.Props

import scarla.utilities.General.range
import scala.collection.immutable.Map

trait DomainSpec {

  type DL = Map[String, (Double, Double)]

  protected val _DEFAULT_LIMITS: DL
  def getLimits: DL = _DEFAULT_LIMITS
  def getLimits(dl: DL): DL = dl ++ _DEFAULT_LIMITS

  val N_ACTIONS: Int
  lazy val ALL_AIDS: Vector[Int] = range(N_ACTIONS)
}
