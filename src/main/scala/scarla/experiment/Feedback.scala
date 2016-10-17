package scarla.experiment

import scala.collection.immutable.Map

case class Feedback(data: Map[String, Any]) {

  def get[T](k: String): T = data(k).asInstanceOf[T]
  def string(k: String): String = get(k).toString
}
