package scarla.experiment

import akka.actor.{Actor, ActorRef, Props}

import scarla.agent.Agent
import scarla.domain.{Domain, State}

object Episode {

  sealed abstract trait Instruction
  final case object Run extends Instruction
}

abstract class Episode(val eid: Int,
                       val agent: ActorRef,
                       val domain: ActorRef)
  extends Actor with akka.actor.ActorLogging {

  import Episode._

  var returnRef: Option[ActorRef] = None

  var stepCounter: Int = 0
  def stepLimit: Int = 2000


  protected def _run = {
    returnRef match {
      case None    =>
        returnRef = Some(sender())

        domain ! Domain.GetState

      case Some(ref) =>
        log.warning("Episode already in progress.")
    }
  }


  protected def _handle[T](msg: T)
  protected def _handle(d: Domain.DoAction) =
    domain ! d

  protected def _log = {}

  protected def _publish =
    context.system.eventStream.publish(Feedback(Map(
      "eid" -> eid,
      "type" -> this.getClass.getSimpleName,
      "n_steps" -> stepCounter)))


  def receive = {
    case i: Instruction => i match {
      case Run => _run
    }

    case s: State => _handle(s)
    case t: State.Transition =>
      stepCounter += 1

      _handle(t)

      if (t.ns.isTerminal || stepCounter >= stepLimit) {
        _log
        _publish

        if (returnRef.isDefined)
          returnRef.get ! akka.actor.Status.Success
      }

    case d: Domain.DoAction if (stepCounter < stepLimit) => _handle(d)
  }
}


object TrainingEpisode {

  def props(eid: Int, agent: ActorRef, domain: ActorRef): Props =
    Props(new TrainingEpisode(eid, agent, domain))
}

class TrainingEpisode(eid: Int, agent: ActorRef, domain: ActorRef)
  extends Episode(eid, agent, domain) {

  import Episode._

  def _handle[T](msg: T) =
    agent ! Agent.Learn(msg)

  override def _log =
    log.info("Training episode %d complete: %d steps".format(eid, stepCounter))
}


object EvaluationEpisode {

  def props(eid: Int, agent: ActorRef, domain: ActorRef): Props =
    Props(new EvaluationEpisode(eid, agent, domain))
}

class EvaluationEpisode(eid: Int, agent: ActorRef, domain: ActorRef)
  extends Episode(eid, agent, domain) {

  import Episode._

  def _handle[T](msg: T) =
    agent ! Agent.Evaluate(msg)

  override def _log =
    log.info("Evaluation %d complete: %d steps.".format(eid, stepCounter))
}
