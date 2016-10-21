package scarla.domain.cards

import scala.Enumeration

object Rank extends Enumeration {
  val Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten,
      Jack, Queen, King = Value
}

object Suit extends Enumeration {
  val Hearts, Clubs, Diamonds, Spades = Value
}

case class Card(rank: Rank.Value, suit: Suit.Value) {
  override def toString: String =
    rank + "@" + suit
}
