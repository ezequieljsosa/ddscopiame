package ar.utn.dds.copiame.apps;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import ar.utn.dds.copiame.domain.AnalisisDeCopia;
import ar.utn.dds.copiame.persist.AnalisisJPARepository;
import ar.utn.dds.copiame.persist.PersistenceUtils;

public class CopiaMeWorker extends DefaultConsumer {

	private String queueName;
	private EntityManagerFactory entityManagerFactory;

	protected CopiaMeWorker(Channel channel, String queueName, EntityManagerFactory entityManagerFactory) {
		super(channel);
		this.queueName = queueName;
		this.entityManagerFactory = entityManagerFactory;
	}

	private void init() throws IOException {
		// Declarar la cola desde la cual consumir mensajes
		this.getChannel().queueDeclare(this.queueName, false, false, false, null);
		// Consumir mensajes de la cola
		this.getChannel().basicConsume(this.queueName, false, this);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
			throws IOException {
		
		// Confirmar la recepción del mensaje a la mensajeria
		this.getChannel().basicAck(envelope.getDeliveryTag(), false);
		
		// Leer el mejaje
		String analisisId = new String(body, "UTF-8");
		
		// Obtengo el analisis a procesar
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		entityManager.getTransaction().begin();
		
		AnalisisJPARepository repo = new AnalisisJPARepository(entityManager);		
		
		AnalisisDeCopia ads = repo.findById(analisisId);
		
		// Proceso el analisis
		ads.procesar();
		
		
		entityManager.getTransaction().commit();
		entityManager.close();
		
	}

	public static void main(String[] args) throws Exception {
		// Establecer la conexión con CloudAMQP
		Map<String, String> env = System.getenv();
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(env.get("QUEUE_HOST"));
		factory.setUsername(env.get("QUEUE_USERNAME"));
		factory.setPassword(env.get("QUEUE_PASSWORD"));
		// En el plan mas barato, el VHOST == USER
		factory.setVirtualHost(env.get("QUEUE_USERNAME"));
		String queueName = env.get("QUEUE_NAME");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		EntityManagerFactory entityManagerFactory =  PersistenceUtils.createEntityManagerFactory();
		
		CopiaMeWorker worker = new CopiaMeWorker(channel,queueName,entityManagerFactory);
		worker.init();
		
	}

}
