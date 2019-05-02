package sk.bsmk.hi

import java.util.UUID

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer}
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}

import scala.concurrent.Future
import scala.collection.JavaConverters._

class TransactionAvroProducer(bootstrapServers: String, schemaRegistryUrl: String, topic: String)(
    implicit val system: ActorSystem,
    implicit val materializer: Materializer
) {

  private val kafkaAvroSerDeConfig = Map[String, Any] {
    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> schemaRegistryUrl
  }

  private val config = system.settings.config.getConfig("akka.kafka.producer")

  private val producerSettings: ProducerSettings[String, SpecificRecord] = {
    val kafkaAvroSerializer = new KafkaAvroSerializer()
    kafkaAvroSerializer.configure(kafkaAvroSerDeConfig.asJava, false)
    val serializer = kafkaAvroSerializer.asInstanceOf[Serializer[SpecificRecord]]

    ProducerSettings(system, new StringSerializer, serializer)
      .withBootstrapServers(bootstrapServers)
  }

  def run(): Future[Done] =
    Source(1 to 100)
      .map(amount => MonetaryTransaction(UUID.randomUUID().toString, "ECommerce", amount.toDouble, "EUR"))
      .map(value => new ProducerRecord[String, SpecificRecord](topic, value.id, value))
      .runWith(Producer.plainSink(producerSettings))

}
