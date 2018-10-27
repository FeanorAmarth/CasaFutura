package com.CasaFutura.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.Utente;
import com.CasaFutura.Repository.RRuolo;
import com.CasaFutura.Repository.RUtente;

@Service("UtenteService")
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private RUtente utenteRepository;
    
    @Autowired
    private RRuolo roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Utente findUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    @Override
    public void saveUtente(Utente utente) {
        utente.setPassword(bCryptPasswordEncoder.encode(utente.getPassword()));
        utenteRepository.save(utente);
    }

    @Override
    public Utente findUtenteById(int id) {
        return utenteRepository.findById(id);
    }

    @Override
    public void updateUtente(Utente utente) {
        utenteRepository.save(utente);
    }

    @Override
    public List<Utente> getAllUtente() {
        return utenteRepository.findAll();
    }

    @Override
    public void deleteUtente(int id) {
        utenteRepository.removeById(id);
    }
    
    @Override
    public List<Utente> findUnCtrl(int id){
    	return utenteRepository.findUnCtrl(id);
    }
    
    @Override
    public List<Utente> findUnCtrlAll(){
    	return utenteRepository.findAllUsrTyp(4);
    }
    
    @Override
    public List<Utente> findParentAll(){
    	return utenteRepository.findAllUsrTyp(3);
    }
    @Override
    public String isEdu(String mail) {
    	System.out.println("mannaggia cristo sulla croce");
    	return utenteRepository.isEdu(mail);
    }
    
    @Override
    public void setEdu(String mail) {
    	utenteRepository.setEdu(mail);
    }
    
    @Override
    public int isEmptMntrUtCtrl() {
    	return utenteRepository.isUctrlEduEmpt();
    };
    
}

