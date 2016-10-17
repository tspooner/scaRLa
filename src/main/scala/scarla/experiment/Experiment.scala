package scarla.experiment

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.pattern.ask
import akka.actor.{Actor, ActorRef, Props, Status}
import akka.util.Timeout

import scarla.agent.Agent
import scarla.domain.Domain

object Experiment {

  sealed abstract trait Instruction
  final case class Train(nEpisodes: Int) extends Instruction
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


  private val _trainEpisode: PartialFunction[Int, ActorRef] = {
    case eid: Int =>
      context.actorOf(TrainingEpisode.props(eid, agent, domain),
                      "episode-%d".format(eid))
  }

  private val _evalEpisode: PartialFunction[Int, ActorRef] = {
    case eid: Int =>
      context.actorOf(EvaluationEpisode.props(eid, agent, domain),
                      "evaluation-%d".format(eid))
  }


  override def preStart() = {
    super.preStart()

    context.system.eventStream.subscribe(self, classOf[Feedback])
  }


  protected def _run(nEpisodes: Int,
                     newEpisode: (Int) => ActorRef): Unit = {
    implicit val timeout = Timeout(10.seconds)

    def runEpisode(eid: Int) = {
      val episode = newEpisode(eid)

      Await.result(episode ? Episode.Run, timeout.duration)
      context stop episode

      domain ! Domain.Reset
    }

    1 to nEpisodes foreach runEpisode

    sender() ! Status.Success
  }


  def receive = {
    case instruction: Instruction => instruction match {
      case Train(nEpisodes)    => _run(nEpisodes, _trainEpisode)
      case Evaluate(nEpisodes) => _run(nEpisodes, _evalEpisode)
    }
  }
}
