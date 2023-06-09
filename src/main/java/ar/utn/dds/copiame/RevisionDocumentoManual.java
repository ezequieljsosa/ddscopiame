package ar.utn.dds.copiame;

import javax.persistence.ManyToOne;

public class RevisionDocumentoManual extends RevisionDocumento {

	@ManyToOne
	private Revisor revisor;
	
	public RevisionDocumentoManual(ParDocumentos par,Revisor revisor) {
		super(par);
		this.revisor = revisor;
	}

	public Revisor getRevisor() {
		return revisor;
	}

	public void setRevisor(Revisor revisor) {
		this.revisor = revisor;
	}
	
	
	
}
