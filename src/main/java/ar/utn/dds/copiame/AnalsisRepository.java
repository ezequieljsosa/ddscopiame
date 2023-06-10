package ar.utn.dds.copiame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface AnalsisRepository {
	
	public void save(AnalsisDeCopia analisis) ;
	
	public AnalsisDeCopia findById(String id) ;
	
	public Collection<AnalsisDeCopia> all() ;
	
}
