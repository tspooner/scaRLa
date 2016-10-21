package scarla.domain

import akka.actor.Props
import scarla.domain.cards.{Deck, Hand, Blackjack => Eval, Card}

import scala.collection.immutable.Map

object Blackjack extends DomainSpec {

  // Blackjack:
  private val ACTIONS = Vector('hit, 'stand)

  def toState(dealer: Hand, player: Hand): State = {
    val sv: Map[String, Double] = Map(
      "value" -> player.value,
      "upcard" -> Eval.value(dealer.tail),
      "hasAce" -> (if (player.hasAce) 1.0 else 0.0)
    )

    if (player.isBust)
      State(sv, Vector(), true)
    else
      State(sv, ALL_AIDS, false)
  }

  // DomainSpec:
  protected val _DEFAULT_LIMITS = Map(
    "value" -> (0.0, 21.0),
    "upcard" -> (1.0, 10.0),
    "hasAce" -> (0.0, 1.0)
  )

  val N_ACTIONS = ACTIONS.size

  // Akka:
  def props = Props(classOf[Blackjack])
}

class Blackjack extends Domain {
  import Blackjack._

  var deck: Deck = (new Deck).shuffled
  private def nextCard: Card = {
    val out = deck.deal()
    deck = out._2
    out._1(0)
  }

  var player: Hand = nextCard :: nextCard :: (new Hand(Eval))
  var dealer: Hand = nextCard :: nextCard :: (new Hand(Eval))


  var state = initialState
  def initialState = toState(dealer, player)

  override def _reset = {
    deck = (new Deck).shuffled
    player = nextCard :: nextCard :: (new Hand(Eval))
    dealer = nextCard :: nextCard :: (new Hand(Eval))

    super._reset
  }

  def next(aid: Int): State = {
    val ns = if (ACTIONS(aid) == 'hit) {player = nextCard :: player; toState(dealer, player)}
             else state.terminated

    if (ns.isTerminal) {
      // Play out dealer strategy:
      while (dealer.value < 18 && !dealer.isBust)
        dealer = nextCard :: dealer
    }

    ns
  }

  def reward(s: State, ns: State): Double = ns.isTerminal match {
    case true =>
      if (Eval.winner(player, dealer)) 1.0
      else -1.0

    case false => 0.0
  }
}
