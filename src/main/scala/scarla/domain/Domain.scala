package scarla.domain

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props

object Domain {

  sealed abstract trait Instruction
  final case class DoAction(aid: Int) extends Instruction

  def props: Props = Props(classOf[Domain])
}

class Domain extends Actor with akka.actor.ActorLogging {
  import Domain._

  def doAction(aid: Int) =
    log.info("Doing action %d".format(aid))

  def receive = {
    case instruction: Instruction => instruction match {
      case DoAction(aid) => doAction(aid)
    }
  }
}
