package scarla

import akka.actor.ActorSystem
import akka.actor.Status
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

import scarla.experiment.Experiment
import scarla.agent.policy.Policy
import scarla.agent.model.Model
import scarla.agent.Agent

object Main extends App {

  // Intialise the ActorSystem:
  val system = ActorSystem("scaRLa")

  // Basics:
  val name   = "experiment"
  val conf   = ConfigFactory.load(name)

  // Domain:
  val domainProps = conf.getString("domain.type").toLowerCase match {
    case "mountain_car" => scarla.domain.MountainCar.props

    case x              =>
      system.terminate
      throw new RuntimeException("Unknown domain type: %s".format(x))
  }

  // - Policy:
  val policy = conf.getString("agent.policy.type") match {
    case _ => new Policy
  }

  // - Model:
  val model = conf.getString("agent.model.type") match {
    case _ => new Model
  }

  // Agent:
  val agentProps = conf.getString("agent.type") match {
    case _ => Agent.props(policy, model)
  }


  // Start the experiment:
  val exp = system.actorOf(Experiment.props(domainProps, agentProps), name)

  val nTrainEpisodes = 5
  val nEvaluationEpisodes = 1
  implicit val timeout = Timeout((10*nTrainEpisodes).seconds)

  Await.result(exp ? Experiment.Run(5), timeout.duration) match {
    case Status.Success    => system.terminate
    case Status.Failure(e) => throw e
  }
}
