package scarla.domain

import scala.collection.mutable.Set

import akka.actor.{Actor, ActorRef}

object Domain {

  sealed abstract trait Instruction
  final case object Reset extends Instruction
  final case object GetState extends Instruction
  final case class DoAction(aid: Int) extends Instruction
}

abstract class Domain(val spec: DomainSpec) extends Actor {
  import Domain._

  var state: State = initialState


  def initialState: State
  def next(aid: Int): State
  def reward(s: State, ns: State): Double


  protected def _reset = {
    state = initialState
  }


  def receive = {
    case instruction: Instruction => instruction match {
      case Reset => _reset

      case GetState => sender() ! state
      case DoAction(aid) =>
        val ls = state
        state = next(aid)

        val r = reward(ls, state)

        sender() ! State.Transition(ls, aid, r, state)
    }
  }
}

abstract class MultiAgentDomain(spec: DomainSpec, val nAgents: Int)
  extends Domain(spec)
