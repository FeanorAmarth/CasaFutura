package com.CasaFutura.Service;

import java.util.List;

import javax.transaction.Transactional;

import com.CasaFutura.Entity.SafePath;
import com.CasaFutura.Entity.Utente;

public interface UtenteCtrlService {
	
	/* restituisce l'utente controllato con l'id specificato*/
	public Utente GetUserCtrlById(int id);
	
	/* restituisce l'utente controllato con il numero di telefono passato*/
	public Utente GetUserCtrlByPhone(String phone);
	
	/* controlla che la posizione da controllare sia all'interno di un area sicura
	public int IsInSArea(String sarea, String PosCrtl);*/
	
	/* restituisce una stringa (secondo il formato WKT) che identifica l'isieme dei poligoni (MULTIPOLYGON)
	 * che rappresentanti le aree sicure dell'utente controllato*/
	public String GetMultiPolyWKT(Utente uctrl);
	
	/*restituisce il percorso sicuro assegnato all'utente con l'id dato */
	public SafePath GetMySPathById(Utente uctrl, int id);
	
	/*salva su db l'utente controllato specificato*/
	public void SaveUserCtrl(Utente uctrl);
	
	public List<Utente> findAll();
	
}
