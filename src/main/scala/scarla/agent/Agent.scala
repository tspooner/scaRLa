package scarla.agent

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props

import scarla.agent.policy.Policy
import scarla.agent.model.Model

object Agent {

  sealed abstract trait Instruction
  final case object Act extends Instruction
  final case object Learn extends Instruction

  def props(policy: Policy, model: Model): Props =
    Props(new Agent(policy, model))
}

class Agent(val policy: Policy, val model: Model)
extends Actor with akka.actor.ActorLogging {

  import Agent._

  def receive = {
    case instruction: Instruction => instruction match {
      case Act    => log.info("Act")
      case Learn  => log.info("Learn")
      case _      => log.info("_")
    }
  }
}
