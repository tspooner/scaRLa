package scarla.agent

import akka.actor.{ActorRef, Actor}

import scarla.domain.{Domain, State}
import scarla.mapping.Mapping
import scarla.policy.{Policy, Greedy}

object Agent {

  sealed abstract trait Instruction
  final case class Learn(msg: Any)
  final case class Evaluate(msg: Any)
}

abstract class Agent(val mapping: Mapping, val policy: Policy)
  extends Actor with akka.actor.ActorLogging {

  import Agent._

  val greedyPolicy: Policy = new Greedy(mapping)

  def act(s: State): Int = policy.pi(s)
  def greedy(s: State): Int = greedyPolicy.pi(s)

  def learn(s: State, aid: Int, r: Double, ns: State)


  def receive = {
    case Learn(msg) => msg match {
      case s: State =>
        sender() ! Domain.DoAction(act(s))

      case State.Transition(s, aid, r, ns) =>
        learn(s, aid, r, ns)

        if (ns.isTerminal)
          policy.terminalUpdate(s)
        else
          sender() ! Domain.DoAction(act(ns))
    }

    case Evaluate(msg) => msg match {
      case s: State =>
        sender() ! Domain.DoAction(greedy(s))

      case State.Transition(s, aid, r, ns) =>
        if (!ns.isTerminal)
          sender() ! Domain.DoAction(greedy(ns))
    }
  }
}
