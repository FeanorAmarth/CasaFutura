package com.CasaFutura.Repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.CasaFutura.Entity.PointOfInterest;

public interface RPointOfInterest extends JpaRepository<PointOfInterest,Long>{

	PointOfInterest findById(int id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE point_of_interest FROM point_of_interest " + 
			"WHERE point_of_interest.id=?1", nativeQuery=true)
	void deletePOIById(int id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE spath_poi FROM spath_poi " + 
	"INNER JOIN point_of_interest ON  spath_poi.poi_id=point_of_interest.id "+
	"WHERE point_of_interest.id=?1", nativeQuery=true)
	void deleteJoinById(int id);
	
	
	/*sfruttando spatial mysql, si cercano i POI associati al percorso specificato (id) che 
	 * siano i piu' vicini alla posizione attuale dell'utente controllato. */
	
	@Query(value="SELECT *, ST_Distance(ST_GeomFromText(?2),point_of_interest.pos) as distanza " + 
			"FROM point_of_interest " + 
			"INNER JOIN spath_poi ON spath_poi.poi_id=point_of_interest.id " + 
			"WHERE spath_poi.spath_id=?1 ORDER BY distanza LIMIT 1", nativeQuery=true)
	PointOfInterest getNextPoi(int id, String PosCrtl);

	@Query(value="SELECT point_of_interest.* FROM point_of_interest, spath_poi "+
	"WHERE point_of_interest.id!=spath_poi.poi_id", nativeQuery=true)
	Set<PointOfInterest> findPathlessPoi();
	
	@Transactional
    public void removeById(int id);
	
	List<PointOfInterest> findAll();
	
}
