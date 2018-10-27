package com.CasaFutura.Service;

import java.util.List;
import java.util.Set;

import com.CasaFutura.Entity.PointOfInterest;

public interface PointOfInterestService {
	
	/* restituisce un JSON con l'elenco di tutti i POI specificati. questa funzione e' attulemente inutilizzata
	 * perche' frutto di un tentativo di gestione dei punti di interesse fallito... non avevo compreso come il client
	 * gestisse la lista di poi rievuta*/
	
	String getPoisJson(Set<PointOfInterest> POIs);
	
	/* restituisce il punto di interesse, associato al percorso specificato, piu' vicino all'attuale posizione dell'utente*/
	
	String getNextPoiJson(int id, String PosCrtl);
	
	/*salva su db il punto d'interesse specificato*/
	
	PointOfInterest SavePoi(PointOfInterest POI);
	
	PointOfInterest getPOIbyId(int id);
	
	void dellPoi(PointOfInterest poi);
	
	boolean deleteImgPoi(String name);
	
	List<PointOfInterest> findAll();
	
}
