package scarla.experiment

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.actor.Status
import akka.actor.Props
import akka.util.Timeout
import akka.event.Logging

import scarla.agent.Agent
import scarla.domain.Domain

class Experiment(val name: String,
                 val agentProps: Props,
                 val domainProps: Props) {

  val system = ActorSystem(name)
  val log    = Logging.getLogger(system, this)

  val agent  = system.actorOf(agentProps, "agent")
  val domain = system.actorOf(domainProps, "domain")

  def run(nEpisodes: Int): Unit = {
    implicit val timeout = Timeout(10.seconds)

    def runEpisode(eid: Int) = {
      val episode =
        system.actorOf(Episode.props(eid, agent, domain),
                       "episode-%d".format(eid))

      Await.result(episode ? Episode.RunAndReply, timeout.duration) match {
        case Status.Success =>
          system stop episode
      }
    }

    1 to nEpisodes foreach runEpisode
  }

  def terminate = system.terminate
}
