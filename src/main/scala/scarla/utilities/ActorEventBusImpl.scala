package scarla.utilities

import akka.actor.{ActorSystem, ActorRef}
import akka.event.{ActorEventBus, ActorClassifier, ManagedActorClassification}

final case class Notification(ref: ActorRef, event: Any)

class ActorEventBusImpl(val system: ActorSystem) extends ActorEventBus
  with ActorClassifier with ManagedActorClassification {

  type Event = Notification

  override protected def classify(event: Event): ActorRef = event.ref

  override def mapSize() = 64
}
