package scarla.experiment

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props

object Episode {

  sealed abstract trait Instruction
  final case object RunAndReply extends Instruction

  def props(eid: Int, agent: ActorRef, domain: ActorRef): Props =
    Props(new Episode(eid, agent, domain))
}

class Episode(val eid: Int,
              val agent: ActorRef,
              val domain: ActorRef) extends Actor with akka.actor.ActorLogging {

  import Episode._

  var replyRef: Option[ActorRef] = None

  def receive = {
    case instruction: Instruction => instruction match {
      case RunAndReply =>
        log.info("Running episode %d".format(eid))

        replyRef = Some(sender())

        replyRef match {
          case Some(sender) =>
            sender ! akka.actor.Status.Success

          case _ =>
        }
    }
  }
}
