package scarla.experiment

import akka.actor.{Actor, ActorRef, Props}

import scarla.agent.Agent
import scarla.domain.{Domain, State}

object Episode {

  sealed abstract trait Instruction
  final case object Run extends Instruction

  def props(eid: Int, agent: ActorRef, domain: ActorRef): Props =
    Props(new Episode(eid, agent, domain))
}

class Episode(val eid: Int,
              val agent: ActorRef,
              val domain: ActorRef)
  extends Actor with akka.actor.ActorLogging {

  import Episode._

  var returnRef: Option[ActorRef] = None

  var stepCounter: Int = 0
  def stepLimit: Int = 1000


  protected def _run = {
    returnRef match {
      case None    =>
        returnRef = Some(sender())

        domain ! Domain.GetState

      case Some(ref) =>
        log.warning("Episode already in progress.")
    }
  }


  protected def _handle[T](msg: T) =
    agent ! Agent.Learn(msg)

  protected def _handle(d: Domain.DoAction) =
    domain ! d

  protected def _log =
    log.info("Training episode %d complete: %d steps".format(eid, stepCounter))


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

        if (returnRef.isDefined)
          returnRef.get ! akka.actor.Status.Success
      }

    case d: Domain.DoAction if (stepCounter < stepLimit) => _handle(d)
  }
}
