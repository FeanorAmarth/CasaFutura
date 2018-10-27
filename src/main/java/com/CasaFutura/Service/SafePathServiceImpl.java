package com.CasaFutura.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.SafePath;
import com.CasaFutura.Repository.RSafePath;

@Service("SafePathService")
public class SafePathServiceImpl implements SafePathService{
	
	@Autowired
	private RSafePath Rspath;
	
	@Override
	public int IsInSpath(String spath, String PosCrtl) {
		return Rspath.getIsInSPath(spath, PosCrtl);
	}
	
	@Override
	public SafePath findById(int id) {
		return Rspath.findById(id);
	}
	
	@Override
	public void delSafePathById(int id) {
		Rspath.deleteJoinCtrlUsrById(id);
		Rspath.deleteJoinPoiById(id);
		Rspath.removeById(id);
	}
	
}
