package ar.utn.dds.copiame;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EvaluadorDeCopia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	protected EvaluadorDeCopia() {
		super();
	}

	protected long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public abstract void procesar(List<ParDocumentos> pares);

}
