package com.CasaFutura.Service;

import com.CasaFutura.Entity.SafePath;

public interface SafePathService {
	
	/* verifica che la posizione dell'utente da controllare sia all'interno di un percorso sicuro*/
	
	public int IsInSpath(String spath, String PosCrtl);
	
	public SafePath findById(int id);
	
	public void delSafePathById(int id);
	
}
