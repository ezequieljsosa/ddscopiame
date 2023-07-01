package ar.utn.dds.copiame.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.utn.dds.copiame.domain.AnalisisDeCopia;
import ar.utn.dds.copiame.domain.EvaluadorDeCopiaAutomatico;
import ar.utn.dds.copiame.domain.EvaluadorDeCopiaManual;
import ar.utn.dds.copiame.domain.Revisor;
import ar.utn.dds.copiame.mq.MQUtils;
import ar.utn.dds.copiame.persist.AnalisisJPARepository;
import ar.utn.dds.copiame.persist.AnalsisRepository;
import ar.utn.dds.copiame.persist.Lote;
import ar.utn.dds.copiame.persist.UnzipUtility;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AnalisisAddController implements Handler {

	private EntityManagerFactory entityManagerFactory;
	private MQUtils mqutils;

	public AnalisisAddController(EntityManagerFactory entityManagerFactory, MQUtils mqutils) {
		super();
		this.entityManagerFactory = entityManagerFactory;
		this.mqutils = mqutils; 
	}
	
	@Override
	public void handle(Context ctx) throws Exception {
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		AnalisisJPARepository repo = new AnalisisJPARepository(entityManager);

		
		// Proceso de parámetros de entrada
		String destDirectory = "/tmp/unlugar";
		System.out.print(ctx.uploadedFiles());
		UnzipUtility.unzip(ctx.uploadedFile("file").content() ,destDirectory);
		
		// Armado del Análisis
		Lote lote = new Lote(destDirectory);
		lote.validar();
		lote.cargar();
		float umbral = 0.5f;
		AnalisisDeCopia analisis = new AnalisisDeCopia(umbral, lote);
		analisis.addEvaluador(new EvaluadorDeCopiaAutomatico());
		Revisor revisor = new Revisor();
		List<Revisor> revisores = Arrays.asList(revisor);
		
		EvaluadorDeCopiaManual eval = new EvaluadorDeCopiaManual(revisores,1.0);
		analisis.addEvaluador(eval);		
		
		// genero los pares de documentos del lote
		analisis.generarPares();
		
		// Guardado
		entityManager.getTransaction().begin();
		repo.save(analisis);
		entityManager.getTransaction().commit();
		entityManager.close();
		
		mqutils.publish(analisis.getId());
		
		
		// Armado de la respuesta
		Map<String,String> rta = new HashMap<String, String>();
		rta.put("analisis", analisis.getId());
		rta.put("terminado", analisis.finalizado().toString());
		ctx.json(rta);

	}

}
