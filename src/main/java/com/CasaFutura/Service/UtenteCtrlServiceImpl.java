package com.CasaFutura.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.SafeArea;
import com.CasaFutura.Entity.SafePath;
import com.CasaFutura.Entity.Utente;
import com.CasaFutura.Repository.RUtente;

@Service("UtenteCtrlService")
public class UtenteCtrlServiceImpl implements UtenteCtrlService{
	
	@Autowired
	private RUtente Uctrl;

	@Override
	public Utente GetUserCtrlById(int id) {
		return Uctrl.findById(id);
	}
	
	@Override
	public Utente GetUserCtrlByPhone(String phone) {
		return Uctrl.findByPhone(phone);
	}

	@Override
	public String GetMultiPolyWKT(Utente uctrl) {
		
		String multipoly="MULTIPOLYGON(";
		Set<SafeArea> SFA=uctrl.getSarea();
		
		if(!SFA.isEmpty()) {
			Iterator<SafeArea> it=SFA.iterator();
			for(int i=0;i<SFA.size();i++) {
				String tmp=it.next().getSArea().toString();
				if(i!=SFA.size()-1) {
					multipoly+=tmp.substring(7, tmp.length())+",";
				}else {
					multipoly+=tmp.substring(7, tmp.length())+")";
				}
			}
		}else {
			multipoly="";
		}
		System.out.println(multipoly);
		return multipoly;
	}
	
	@Override
	public SafePath GetMySPathById(Utente uctrl, int id) {
		
		Set<SafePath> SFP=uctrl.getSpath();
		SafePath path=null;
		
		if(!SFP.isEmpty()){
			Iterator<SafePath> it=SFP.iterator();
			for(int i=0;i<SFP.size();i++) {
				path=it.next();
				if(path.getId()==id)
					break;
			}
		}
		
		return path;
	}

	@Override
	@Transactional
	public void SaveUserCtrl(Utente uctrl) {
		Uctrl.save(uctrl);
	}
	
	@Override
	public List<Utente> findAll() {
		return Uctrl.findAll();
	}

}
