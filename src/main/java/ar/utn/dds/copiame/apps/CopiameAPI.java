package ar.utn.dds.copiame.apps;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.persistence.EntityManagerFactory;

import ar.utn.dds.copiame.controllers.AnalisisAddController;
import ar.utn.dds.copiame.controllers.AnalisisListController;
import ar.utn.dds.copiame.controllers.RevisorGetController;
import ar.utn.dds.copiame.controllers.RevisorPostController;
import ar.utn.dds.copiame.mq.MQUtils;
import ar.utn.dds.copiame.persist.PersistenceUtils;
import io.javalin.Javalin;

public class CopiameAPI {

	public static EntityManagerFactory entityManagerFactory;

	public static void main(String[] args) throws Exception {
		entityManagerFactory =  PersistenceUtils.createEntityManagerFactory();
		
		Map<String, String> env = System.getenv();
		MQUtils mqutils = new MQUtils(
				env.get("QUEUE_HOST"),
				env.get("QUEUE_USERNAME"),
				env.get("QUEUE_PASSWORD"),				
				env.get("QUEUE_USERNAME"),
				env.get("QUEUE_NAME")
		);
		mqutils.init();
		
		Integer port = Integer.parseInt(System.getProperty("PORT", "8080"));

		Javalin app = Javalin.create().start(port);

		app.get("/analisis", new AnalisisListController(entityManagerFactory));
		app.post("/analisis", new AnalisisAddController(entityManagerFactory,mqutils));

		app.get("/revisor/{id}", new RevisorGetController(entityManagerFactory));
		app.post("/revisor/", new RevisorPostController(entityManagerFactory));
		
		//app.get("/revisor/{id}/revision", new RevisorRevisionesListController(entityManagerFactory));
		//app.post("/revisor/{id}/revision", new RevisorAddRevisionController(entityManagerFactory));
		// app.get("/revisor/{id}/revision/{rev}/file/{fileid}", new
		// RevisorRevisionFilesController(repo));

	}

	

}
