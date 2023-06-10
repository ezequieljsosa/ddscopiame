package ar.utn.dds.copiame.apps;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class CopiaMeWorker {

	private static final String QUEUE_NAME = "nombre_de_la_cola";

	public static void main(String[] args) throws IOException, TimeoutException {
		// Establecer la conexión con CloudAMQP
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("tu_host_cloudamqp");
		factory.setUsername("tu_usuario");
		factory.setPassword("tu_contraseña");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// Declarar la cola desde la cual consumir mensajes
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// Crear un consumidor
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				// Procesar el mensaje recibido y generar el resultado en formato JSON
				String result = processMessage(message);

				// Escribir el resultado en el archivo JSON
				// writeResultToJsonFile(result);

				// Confirmar la recepción del mensaje
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		// Consumir mensajes de la cola
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}

	private static String processMessage(String message) {
		// Lógica de procesamiento del mensaje
		// Retorna el resultado en formato JSON
		return "{ \"resultado\": \"" + message + "\" }";
	}

}
