package scarla.experiment

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props

import scarla.domain.Domain

object Episode {

  sealed abstract trait Instruction
  final case object Run extends Instruction

  def props(eid: Int, agent: ActorRef, domain: ActorRef): Props =
    Props(new Episode(eid, agent, domain))
}

class Episode(val eid: Int,
              val agent: ActorRef,
              val domain: ActorRef) extends Actor with akka.actor.ActorLogging {

  import Episode._

  var returnRef: Option[ActorRef] = None


  def receive = {
    case instruction: Instruction => instruction match {
      case Run =>
        log.info("Running episode %d".format(eid))

        returnRef match {
          case None    =>
            returnRef = Some(sender())

            domain ! Domain.Subscribe
            domain.tell(Domain.GetState, agent)

          case Some(ref) =>
            log.warning("Episode already in progress.")
        }
    }

    case domainStatus: Domain.Status =>
      domain ! Domain.Unsubscribe

      domainStatus match {
        case Domain.Completed =>
          if (returnRef.isDefined)
            returnRef.get ! akka.actor.Status.Success

        case Domain.Failed =>
          if (returnRef.isDefined)
            returnRef.get ! akka.actor.Status.Failure(
              new RuntimeException("Episode %d failed to complete.".format(eid))
              )
      }
  }
}
