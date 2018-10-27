package com.CasaFutura.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.CasaFutura.Entity.Ruolo;
import com.CasaFutura.Entity.Utente;

public interface RUtente extends CrudRepository<Utente,Long> {

	Utente findByEmail(String email);
    
    Utente findById(int id);
    
    Utente findByPhone(String phone);
    
	Utente findEduByEmailAndRuoli(String email, Ruolo ruolo);
	
	@Transactional
	@Modifying
	@Query(value="DELETE uctrl_edu FROM uctrl_edu " + 
	"INNER JOIN utenti ON uctrl_edu.uctrl_id=utenti.id "+
	"WHERE uctrl_edu.id=?1", nativeQuery=true)
	void deleteJoinEduById(int id);
    
    public List<Utente> findAll();
    
    @Transactional
    public List<Utente> removeById(int id);
    
    @Query(value="SELECT * FROM utenti " +
    		"INNER JOIN utente_ruolo ON  utenti.id=utente_ruolo.utente_id "+
    		"INNER JOIN uctrl_edu ON  utenti.id=uctrl_edu.uctrl_id "+
			"WHERE utente_ruolo.ruolo_id=4 AND uctrl_edu.edu_id!=?1 "+
    		"AND utenti.id NOT IN ( "+
				"SELECT utenti.id FROM utenti "+
				"INNER JOIN uctrl_edu ON  utenti.id=uctrl_edu.uctrl_id "+
				"WHERE uctrl_edu.edu_id=?1)"
		, nativeQuery=true)
    public List<Utente> findUnCtrl(int id);
    
    
    @Query(value="SELECT utenti.dtype FROM utenti " +
			"WHERE utenti.email=?1", nativeQuery=true)
    public String isEdu(String mail);
	
    @Transactional
	@Modifying
    @Query(value="UPDATE utenti "+ 
    		"SET  utenti.dtype='UtenteEdu' "+
    		"WHERE utenti.email=?1", nativeQuery=true)
    public void setEdu(String email);
    
    @Query(value="SELECT COUNT(uctrl_edu.edu_id) as 'leng' FROM uctrl_edu " +
			"WHERE 1", nativeQuery=true)
    public int isUctrlEduEmpt();
    
    @Query(value="SELECT * FROM utenti " +
    		"INNER JOIN utente_ruolo ON  utenti.id=utente_ruolo.utente_id "+
			"WHERE utente_ruolo.ruolo_id=?1", nativeQuery=true)
    public List<Utente> findAllUsrTyp(int id);
    
}
