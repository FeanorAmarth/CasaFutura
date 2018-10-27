package com.CasaFutura.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.CasaFutura.Entity.SafeArea;


public interface RSafeArea extends JpaRepository<SafeArea,Long>{
	
	SafeArea findById(int id);
	
	@Query(value="SELECT ST_Intersects( ST_GeomFromText( ?1 ),ST_Buffer(ST_GeomFromText(?2),0.00005) ) as isinsarea", nativeQuery=true)
	int getIsInSArea(String sarea, String PosCrtl);
	
	@Transactional
	@Modifying
	@Query(value="DELETE uctrl_area FROM uctrl_area " + 
	"INNER JOIN safe_area ON  uctrl_area.area_id=safe_area.id "+
	"WHERE safe_area.id=?1", nativeQuery=true)
	void deleteJoinCtrlUsById(int id);
	
	
	@Transactional
    public void removeById(int id);
	
}
