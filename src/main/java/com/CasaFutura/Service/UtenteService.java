package com.CasaFutura.Service;

import java.util.List;

import com.CasaFutura.Entity.Utente;

public interface UtenteService {

    public Utente findUtenteByEmail(String email);

    public void saveUtente(Utente utente);
    
    public Utente findUtenteById(int id); 

    public void updateUtente(Utente utente);
    
    public List<Utente> getAllUtente();
    
    public void deleteUtente(int id);
    
    public List<Utente> findUnCtrl(int id);
    
    public List<Utente> findUnCtrlAll();
    
    public List<Utente> findParentAll();
    
    public String isEdu(String mail);
    
    public void setEdu(String mail);
    
    public int isEmptMntrUtCtrl();

}
