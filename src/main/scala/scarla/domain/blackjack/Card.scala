package scarla.domain.blackjack

import scala.Enumeration

object Pip extends Enumeration {
  val Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten,
  Jack, Queen, King, Ace = Value
}

object Suit extends Enumeration {
  val Diamonds, Spades, Hearts, Clubs = Value
}

case class Card(suit: Suit.Value, value: Pip.Value) {
  override def toString: String =
    value + " of " + suit
}
