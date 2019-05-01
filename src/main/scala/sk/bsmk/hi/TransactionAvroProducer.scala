package sk.bsmk.hi

import java.util.UUID

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future

class TransactionAvroProducer(bootstrapServers: String, topic: String)(
    implicit val system: ActorSystem,
    implicit val materializer: Materializer
) {

  private val config = system.settings.config.getConfig("akka.kafka.producer")

  private val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)

  def run(): Future[Done] =
    Source(1 to 100)
      .map(amount => MonetaryTransaction(UUID.randomUUID().toString, "ECommerce", amount.toDouble, "EUR"))
      .map(value => new ProducerRecord[String, MonetaryTransaction](topic, value))
      .runWith(???)

}
