import org.scalatest._

import org.scalamock.scalatest.MockFactory

import scarla.domain.cards.{Deck, Card, Rank, Suit}

class DeckSpec extends FlatSpec with Matchers with MockFactory {

  "A Deck" should "have 52 cards" in {
    (new Deck).size should be (52)
  }

  it should "start in new deck order" in {
    val cards =
      (for {s <- Suit.values.toList; r <- Rank.values.toList} yield Card(r, s)).toVector

    (new Deck).cards should be (cards)
  }

  it should "be immutable" in {
    val d = new Deck()
    val (_, nd) = d.deal()

    d.size should be (52)
    nd shouldNot be (d)
  }

  it should "decrease in size with each deal" in {
    val d = new Deck()

    d.deal()._2.size should be (51)
    d.deal(10)._2.size should be (42)
    d.deal(52)._2.size should be (0)
  }
}
