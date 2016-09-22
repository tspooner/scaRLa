package scarla.experiment

import akka.actor.{Actor, ActorRef, Props}

import scarla.agent.Agent
import scarla.domain.{Domain, State}

object EvaluationEpisode {

  def props(eid: Int, agent: ActorRef, domain: ActorRef): Props =
    Props(new EvaluationEpisode(eid, agent, domain))
}

class EvaluationEpisode(eid: Int, agent: ActorRef, domain: ActorRef)
  extends Episode(eid, agent, domain) {

  import Episode._

  override def _handle[T](msg: T) =
    agent ! Agent.Evaluate(msg)

  override def _log =
    log.info("Evaluation %d complete: %d steps.".format(eid, stepCounter))
}
