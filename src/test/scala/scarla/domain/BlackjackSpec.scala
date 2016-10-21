import org.scalatest._
import org.scalamock.scalatest.MockFactory

import akka.actor.ActorSystem
import akka.testkit.TestActorRef

import scarla.domain.{Domain, Blackjack}
import scarla.domain.cards.{Deck, Card, Rank, Suit}

class BlackjackSpec extends FlatSpec with Matchers with MockFactory {

  implicit val system = ActorSystem()

  "The Blackjack domain" should "work" in {
    val ref = TestActorRef(new Blackjack)
    val a = ref.underlyingActor
  }
}
