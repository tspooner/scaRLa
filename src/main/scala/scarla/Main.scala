package scarla

import akka.actor.{ActorSystem, Props}
import akka.actor.Status
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

import scarla.experiment.Experiment
import scarla.agent.TDControlAgent
import scarla.algorithm._
import scarla.mapping._
import scarla.policy._
import scarla.domain._

object Main extends App {

  // Intialise the ActorSystem:
  val system = ActorSystem("scaRLa")

  // Basics:
  val name   = "experiment"
  val conf   = ConfigFactory.load(name)

  // Domain:
  val (domainSpec: DomainSpec, domainProps: Props) =
    conf.getString("domain.type").toLowerCase match {

      case "mountain-car" =>
        (scarla.domain.MountainCar,
          scarla.domain.MountainCar.props)

      case "cliff-walk" =>
        (scarla.domain.CliffWalk,
          scarla.domain.CliffWalk.props)

      case x =>
        system.terminate
        throw new RuntimeException("Unknown domain type: %s".format(x))
  }

  // - Mapping:
  val mapping: Mapping = conf.getString("agent.mapping.type").toLowerCase match {
    case "tabular" => new Tabular(domainSpec)
    case "rbf"     => new RBF(domainSpec)

    case x =>
      system.terminate
      throw new RuntimeException("Unknown mapping type: %s".format(x))
  }

  // - Policy:
  val policy = conf.getString("agent.policy.type").toLowerCase match {
    case "greedy"         => new Greedy(mapping)
    case "epsilon-greedy" => new EpsilonGreedy(mapping)

    case x =>
      system.terminate
      throw new RuntimeException("Unknown policy type: %s".format(x))
  }

  // Agent:
  val agentProps = conf.getString("agent.algorithm.type").toLowerCase match {
    case "q-learn"   => TDControlAgent.props(QLearn, mapping, policy)
    case "sarsa"     => TDControlAgent.props(SARSA, mapping, policy)

    case x =>
      system.terminate
      throw new RuntimeException("Unknown learning algorithm: %s".format(x))
  }


  // Start the experiment:
  val exp = system.actorOf(Experiment.props(domainProps, agentProps), name)

  val nTrainEpisodes = conf.getInt("experiment.nTrainEpisodes")
  val nEvaluationEpisodes = conf.getInt("experiment.nEvaluationEpisodes")

  implicit val timeout = new Timeout(30.minutes)

  Await.result(exp ? Experiment.Train(nTrainEpisodes), timeout.duration) match {
    case Status.Success =>
      Await.result(exp ? Experiment.Evaluate(nEvaluationEpisodes),
                   timeout.duration) match {
        case Status.Success =>
          system.terminate

        case Status.Failure(e) => throw e
      }


    case Status.Failure(e) => throw e
  }
}
