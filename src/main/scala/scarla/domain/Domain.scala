package scarla.domain

import akka.actor.{Actor, ActorRef, ActorLogging}
import scala.collection.mutable.Set

object Domain {

  sealed abstract trait Status
  final case object Completed extends Status
  final case object Failed extends Status

  sealed abstract trait Instruction
  final case object GetState extends Instruction
  final case class DoAction(aid: Int) extends Instruction
  final case object Subscribe extends Instruction
  final case object Unsubscribe extends Instruction
}

abstract class Domain extends Actor with ActorLogging {
  import Domain._

  val subscribers: Set[ActorRef] = Set()

  def initialState: State
  def doAction(aid: Int)

  var state: State = initialState


  def receive = {
    case instruction: Instruction => instruction match {
      case GetState      => sender() ! state
      case DoAction(aid) =>
        doAction(aid)
        sender() ! state

        if (state.isTerminal) {
          state = initialState
          subscribers.map(_.tell(Domain.Completed, self))
        }

      case Subscribe   => subscribers += sender()
      case Unsubscribe => subscribers -= sender()
    }
  }
}
