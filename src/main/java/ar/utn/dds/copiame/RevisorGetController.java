package ar.utn.dds.copiame;

import javax.persistence.EntityManagerFactory;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class RevisorGetController implements Handler {
	
	private EntityManagerFactory entityManagerFactory;
	
	
	public RevisorGetController(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void handle(Context ctx) throws Exception {
		RevisorRepository repo = new RevisorRepository(entityManagerFactory.createEntityManager());
		long revisorId = Long.parseLong(ctx.pathParam("id"));
		Revisor revisor = repo.findById(revisorId);
		ctx.json(revisor);
		

	}

}
