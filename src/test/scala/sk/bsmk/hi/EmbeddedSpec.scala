package sk.bsmk.hi

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

class EmbeddedSpec extends WordSpec with Matchers with EmbeddedKafka {
  "runs with embedded kafka" should {

    "work" in {

      val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)

      implicit val system: ActorSystem = ActorSystem("QuickStart")
      implicit val materializer: ActorMaterializer = ActorMaterializer()

      val producer = new SimpleProducer(
        s"http://localhost:${userDefinedConfig.kafkaPort}",
        "test-topic"
      )

      val done = producer.run()

      Await.result(done, 5.seconds) should be(Done)

    }
  }
}
