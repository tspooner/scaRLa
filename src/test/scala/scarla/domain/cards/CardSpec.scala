import org.scalatest._

import org.scalamock.scalatest.MockFactory

import scarla.domain.cards.{Card, Rank, Suit}

class CardSpec extends FlatSpec with Matchers with MockFactory {

  "A Card" should "have correct equality" in {
    val as1 = Card(Rank.Ace, Suit.Spades)
    val as2 = Card(Rank.Ace, Suit.Spades)
    val th  = Card(Rank.Ten, Suit.Hearts)

    as1 should be (as2)
    as1 shouldNot be (th)
  }
}
