package scarla.agent

import akka.actor.Props

import scarla.mapping.LinearMapping
import scarla.policy.Policy
import scarla.domain.State

object MCControlAgent {

  def props(mapping: LinearMapping, policy: Policy): Props =
    Props(new MCControlAgent(mapping, policy))
}

class MCControlAgent(mapping: LinearMapping, policy: Policy)
  extends Agent(mapping, policy) {

  import MCControlAgent._

  val alpha = 0.01
  val gamma = 0.95

  var history: List[(State, Int)] = List()


  def learn(s: State, aid: Int, r: Double, ns: State) = {
    history = (s, aid) :: history

    if (ns.isTerminal) {
      history.foreach { case t => mapping.update(t._1, t._2, r) }
      history = List()
    }
  }
}
