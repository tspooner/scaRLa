package scarla.domain

import akka.actor.Props
import scarla.domain.blackjack._

object Blackjack extends DomainSpec {

  // Blackjack:
  private val ACTIONS = Vector('hit, 'stand)

  // DomainSpec:
  val N_DIMENSIONS = 3
  val D_LIMITS = Vector((11.0, 21.0), (1.0, 10.0), (0.0, 1.0))

  val N_ACTIONS = 2

  // Akka:
  def props(nAgents: Int) = {
    if (nAgents < 2)
      throw new IllegalArgumentException("Blackjack domain requires at least 2 agents: the dealer and at least 1 player.")

    Props(new Blackjack(nAgents))
  }
}

class Blackjack(nAgents: Int) extends MultiAgentDomain(Blackjack, nAgents) {
  import Blackjack._

  def initialState =
    State(Vector(-0.5, 0.0), ALL_AIDS)

  def next(aid: Int): State =
    initialState

  def reward(s: State, ns: State): Double =
    0.0
}

