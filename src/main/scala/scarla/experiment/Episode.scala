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
                       val domain: ActorRef,
                       logFrequency: Int)
  extends Actor with akka.actor.ActorLogging {

  import Episode._

  var returnRef: Option[ActorRef] = None

  var stepCounter: Int = 0
  def stepLimit: Int = 2000

  var totalReward: Double = 0.0


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

  protected def _log(t: State.Transition) = {}

  protected def _publish(t: State.Transition) =
    context.system.eventStream.publish(Feedback(Map(
      "eid" -> eid,
      "type" -> this.getClass.getSimpleName,
      "n_steps" -> stepCounter,
      "total_reward" -> totalReward)))


  def receive = {
    case i: Instruction => i match {
      case Run => _run
    }

    case s: State => _handle(s)
    case t: State.Transition =>
      stepCounter += 1
      totalReward += t.r

      _handle(t)

      if (t.ns.isTerminal || stepCounter >= stepLimit) {
        _publish(t)
        if (eid % logFrequency == 0)
          _log(t)

        if (returnRef.isDefined)
          returnRef.get ! akka.actor.Status.Success
      }

    case d: Domain.DoAction if (stepCounter < stepLimit) => _handle(d)
  }
}


object TrainingEpisode {

  def props(eid: Int, agent: ActorRef, domain: ActorRef, logFrequency: Int = 1000): Props =
    Props(new TrainingEpisode(eid, agent, domain, logFrequency))
}

class TrainingEpisode(eid: Int, agent: ActorRef, domain: ActorRef, logFrequency: Int = 1000)
  extends Episode(eid, agent, domain, logFrequency) {

  import Episode._

  def _handle[T](msg: T) =
    agent ! Agent.Learn(msg)

  override def _log(t: State.Transition) =
    log.info("Training episode %d (%d steps): %f.".format(eid, stepCounter, totalReward))
}


object EvaluationEpisode {

  def props(eid: Int, agent: ActorRef, domain: ActorRef, logFrequency: Int = 1): Props =
    Props(new EvaluationEpisode(eid, agent, domain, logFrequency))
}

class EvaluationEpisode(eid: Int, agent: ActorRef, domain: ActorRef, logFrequency: Int = 1)
  extends Episode(eid, agent, domain, logFrequency) {

  import Episode._

  def _handle[T](msg: T) =
    agent ! Agent.Evaluate(msg)

  override def _log(t: State.Transition) =
    log.info("Evaluation %d (%d steps): %f.".format(eid, stepCounter, totalReward))
}
