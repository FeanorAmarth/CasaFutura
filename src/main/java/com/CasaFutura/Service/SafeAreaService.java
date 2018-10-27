package com.CasaFutura.Service;

import com.CasaFutura.Entity.SafeArea;

public interface SafeAreaService {
	
	/* controlla che la posizione da controllare sia all'interno di un area sicura*/
	public int IsInSArea(String sarea, String PosCrtl);
	
	public void dellSafePathById(int id);
	
	public SafeArea findById(int id);
	
	
}
