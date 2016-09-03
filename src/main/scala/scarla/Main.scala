import akka.actor.ActorSystem

import com.typesafe.config.ConfigFactory

import scarla.experiment.Experiment
import scarla.agent.policy.Policy
import scarla.agent.model.Model
import scarla.agent.Agent
import scarla.domain.Domain

object Main extends App {

  // Basics:
  val name = "experiment"
  val conf = ConfigFactory.load(name)

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

  // Domain:
  val domainProps = conf.getString("domain.type") match {
    case _ => Domain.props
  }


  // Start the experiment:
  val exp = new Experiment(name, agentProps, domainProps)

  exp run 5

  exp.terminate
}
