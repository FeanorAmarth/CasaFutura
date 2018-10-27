package com.CasaFutura.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.SafeArea;
import com.CasaFutura.Repository.RSafeArea;

@Service("SafeAreaService")
public class SafeAreaServiceImpl implements SafeAreaService{
	
	@Autowired
	private RSafeArea rSarea;

	@Override
	public int IsInSArea(String sarea, String PosCrtl) {
		return rSarea.getIsInSArea(sarea, PosCrtl);
	}
	
	@Override
	@Transactional
	public void dellSafePathById(int id) {
		rSarea.deleteJoinCtrlUsById(id);
		rSarea.removeById(id);
	}
	
	@Override
	public SafeArea findById(int id) {
		return rSarea.findById(id);
	}
	
}
