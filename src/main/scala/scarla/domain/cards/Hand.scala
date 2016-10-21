package scarla.domain.cards

object Hand {

  def containsAce(cards: List[Card]): Boolean =
    cards.exists(c => c.rank == Rank.Ace)
}

class Hand private(val eval: HandEvaluator,
                   val cards: List[Card] = List()) {
  def this(eval: HandEvaluator) = this(eval, List())

  def value: Int = eval.value(cards)
  def isBust: Boolean = eval.isBust(cards)

  def head: Card = cards.head
  def tail: Card = cards.last

  def ::(c: Card): Hand = new Hand(eval, c :: cards)
  def hasAce: Boolean = Hand.containsAce(cards)


  def canEqual(a: Any) = a.isInstanceOf[Hand]

  override def equals(that: Any): Boolean = that match {
    case that: Hand => that.canEqual(this) && this.value == that.value
    case _ => false
  }

  override def toString: String = cards.toString
}
