package com.CasaFutura.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.CasaFutura.Entity.SafePath;

public interface RSafePath extends JpaRepository<SafePath,Long> {

	SafePath findById(int id);
	
	/*sfruttando spatial mysql si verifica che vi sia un intersezione tra: l'area individuata dal percorso sicuro, 
	 * ed un buffer circolare (con un raggio di circa 5m) centrato nella posizione dell'utente controllato*/
	
	@Query(value="SELECT ST_Intersects( ST_Buffer(ST_GeomFromText( ?1 ),0.000025),ST_Buffer(ST_GeomFromText(?2),0.00005) ) as isinspath", nativeQuery=true)
	int getIsInSPath(String spath, String PosCrtl);
	
	@Transactional
	@Modifying
	@Query(value="DELETE safe_path FROM safe_path " + 
			"WHERE safe_path.id=?1", nativeQuery=true)
	void deleteSafePathById(int id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE spath_poi FROM spath_poi " + 
	"INNER JOIN safe_path ON  spath_poi.spath_id=safe_path.id "+
	"WHERE safe_path.id=?1", nativeQuery=true)
	void deleteJoinPoiById(int id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE uctrl_path FROM uctrl_path " + 
	"INNER JOIN safe_path ON  uctrl_path.path_id=safe_path.id "+
	"WHERE safe_path.id=?1", nativeQuery=true)
	void deleteJoinCtrlUsrById(int id);
	
	@Transactional
    public void removeById(int id);
	
}
