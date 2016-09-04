package scarla.experiment

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.pattern.ask
import akka.actor.Actor
import akka.actor.Status
import akka.actor.Props
import akka.util.Timeout

import scarla.agent.Agent
import scarla.domain.Domain

object Experiment {

  sealed abstract trait Instruction
  final case class Run(nEpisodes: Int) extends Instruction
  final case class Evaluate(nEpisodes: Int) extends Instruction

  def props(domainProps: Props, agentProps: Props) =
    Props(new Experiment(domainProps, agentProps))
}

class Experiment(val domainProps: Props,
                 val agentProps: Props)
  extends Actor with akka.actor.ActorLogging {

  import Experiment._

  val domain = context.actorOf(domainProps, "domain")
  val agent  = context.actorOf(agentProps, "agent")

  def run(nEpisodes: Int): Unit = {
    implicit val timeout = Timeout(2.seconds)

    def runEpisode(eid: Int) = {
      val episode =
        context.actorOf(Episode.props(eid, agent, domain),
                        "episode-%d".format(eid))

      Await.result(episode ? Episode.Run, timeout.duration) match {
        case Status.Success =>
          context stop episode

        case f: Status.Failure =>
          sender() forward f
      }
    }

    1 to nEpisodes foreach runEpisode

    sender() ! Status.Success
  }


  def receive = {
    case instruction: Instruction => instruction match {
      case Run(nEpisodes)      => run(nEpisodes)
      case Evaluate(nEpisodes) =>
    }
  }
}
