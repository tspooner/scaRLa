package scarla.domain.cards

trait HandEvaluator {

  def winner(h1: Hand, h2: Hand): Boolean =
    value(h1) > value(h2)

  def value(c: Card): Int
  def value(cs: List[Card]): Int
  def value(h: Hand): Int = value(h.cards)

  def isBust(value: Int): Boolean
  def isBust(cs: List[Card]): Boolean = isBust(value(cs))
  def isBust(h: Hand): Boolean = isBust(h.cards)
}


object Blackjack extends HandEvaluator {

  def value(c: Card): Int = c.rank match {
    case Rank.King => 10
    case Rank.Queen => 10
    case Rank.Jack => 10
    case Rank.Ten => 10
    case Rank.Nine => 9
    case Rank.Eight => 8
    case Rank.Seven => 7
    case Rank.Six => 6
    case Rank.Five => 5
    case Rank.Four => 4
    case Rank.Three => 3
    case Rank.Two => 2
    case Rank.Ace => 1
    case _ => 0
  }

  def value(cs: List[Card]): Int = {
    val (total, hasAce): (Int, Boolean) = cs.foldLeft((0, false)) {
      case (acc, card) => (acc._1 + value(card),
                          acc._2 || card.rank == Rank.Ace)
    }

    if (isBust(total)) 0
    else if (total <= 11 && hasAce) total + 10
    else total
  }

  def isBust(value: Int): Boolean = value > 21 || value == 0
}
