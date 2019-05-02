package sk.bsmk.hi

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import net.manub.embeddedkafka.schemaregistry.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

class TransactionAvroProducerSpec extends WordSpec with Matchers with EmbeddedKafka {

  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  "runs with embedded kafka" should {

    "work" in {

      implicit val config: EmbeddedKafkaConfig = EmbeddedKafkaConfig()

      withRunningKafka {
        val topic = "test-topic"
        val producer = new TransactionAvroProducer(
          s"http://localhost:${config.kafkaPort}",
          s"http://localhost:${config.schemaRegistryPort}",
          topic
        )
        val done = producer.run()

        Await.result(done, 5.seconds) should be(Done)

        // actually failing but message can be seen
        consumeFirstStringMessageFrom(topic) shouldBe MonetaryTransaction("1", "sd", 23.43, "EUR")
      }
    }
  }


}
