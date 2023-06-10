package ar.utn.dds.copiame;

import javax.persistence.EntityManager;

public class DocumentoRepository {

	private EntityManager entityManager ;

	public DocumentoRepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void save(Documento doc) {
		this.entityManager.persist(doc);
	}
	
}
