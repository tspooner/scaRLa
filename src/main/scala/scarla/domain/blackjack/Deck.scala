package scarla.domain.blackjack

import scala.util.Random
import scala.annotation.tailrec

class Deck private(val cards: List[Card]) {
  def this() =
    this(for {s <- Suit.values.toList; v <- Pip.values} yield Card(s, v))

  def shuffle: Deck =
    new Deck(Random.shuffle(cards))

  def deal(n: Int = 1): (Seq[Card], Deck) = {
    @tailrec def loop(count: Int, c: Seq[Card], d: Deck): (Seq[Card], Deck) = {
      if (count == 0 || d.cards == Nil) (c, d)
      else {
        val card :: deck = d.cards
        loop(count-1, c :+ card, new Deck(deck))
      }
    }

    loop(n, Seq(), this)
  }

  override def toString: String =
    "Deck: " + (cards mkString ", ")
}
