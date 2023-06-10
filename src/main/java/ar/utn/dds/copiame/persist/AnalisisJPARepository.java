package ar.utn.dds.copiame;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AnalisisJPARepository implements AnalsisRepository {

	
	private EntityManager entityManager ;

	public AnalisisJPARepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	
	@Override
	public void save(AnalsisDeCopia analisis) {
		this.entityManager.persist(analisis);
		
	}

	@Override
	public AnalsisDeCopia findById(String id) {		
		// Notar que esto no es la PK, queda poco consistente respecto al resto de las clases
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AnalsisDeCopia> criteriaQuery = criteriaBuilder.createQuery(AnalsisDeCopia.class);
		Root<AnalsisDeCopia> root = criteriaQuery.from(AnalsisDeCopia.class);

		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));

		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}
	
	

	@Override
	public Collection<AnalsisDeCopia> all() {
		return null;
	}

}
