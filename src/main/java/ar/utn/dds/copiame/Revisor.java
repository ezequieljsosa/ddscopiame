package ar.utn.dds.copiame;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Revisor {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String nombre;
	private List<RevisionDocumento> revisar;
	
	
	

	public Revisor() {
		super();
		this.revisar = new ArrayList<RevisionDocumento>();
	}

	
	
	protected long getId() {
		return id;
	}



	protected void setId(long id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<RevisionDocumento> getRevisar() {
		return revisar;
	}

	public void setRevisar(List<RevisionDocumento> revisar) {
		this.revisar = revisar;
	}

	public void addRevision(RevisionDocumento rev) {
		this.revisar.add(rev);
	}

}
