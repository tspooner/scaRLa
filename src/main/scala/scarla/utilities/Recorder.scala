package scarla.utilities

import java.io.{File, FileWriter}
import akka.actor.{Actor, Props}

import scarla.experiment.Feedback

object Recorder {

  def props(path: String, cols: List[String]): Props =
    Props(new Recorder(path, cols))
}

case class Recorder(path: String, cols: List[String]) extends Actor {

  val file = new File(path)
  val bw = new FileWriter(file)


  override def postStop = {
    super.postStop()

    bw.close()
  }


  private def _to_json(vals: Map[String, Any]): String = {
    val json = scala.util.parsing.json.JSONObject(vals)

    return json.toString(scala.util.parsing.json.JSONFormat.defaultFormatter)
  }

  def write(fb: Feedback) =
    bw.write(_to_json(fb.data) + '\n')


  def receive = {
    case fb: Feedback => write(fb)
  }
}
