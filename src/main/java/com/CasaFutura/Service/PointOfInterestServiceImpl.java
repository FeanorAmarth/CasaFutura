package com.CasaFutura.Service;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.PointOfInterest;
import com.CasaFutura.Repository.RPointOfInterest;

@Service("PointOfInterestService")
public class PointOfInterestServiceImpl implements PointOfInterestService {
	
	@Autowired
	private RPointOfInterest Rpoi;

	@Override
	public String getPoisJson(Set<PointOfInterest> POIs) {
		
		String JPois="[";
		
		if(!POIs.isEmpty()) {
			Iterator<PointOfInterest> pIt=POIs.iterator();
			for(int i=0; i<POIs.size(); i++) {
				
				PointOfInterest poi=pIt.next();
				JPois+="{\"foto\" : \""+poi.getRimage()+"\", \"the_geom\" : \""+poi.getPos()+"\"}";
				
				if(i<POIs.size()-1) {
					JPois+=",";
				}else {
					JPois+="]";
				}
			}
		}else
			JPois="[]";
		
		return JPois;
	}

	@Override
	public String getNextPoiJson(int id, String PosCrtl) {
		PointOfInterest poi=Rpoi.getNextPoi(id, PosCrtl);
		String JPOI=null;
		if(poi!=null)
			JPOI="[{ \"id\" : "+poi.getId()+", \"descrizione\" : \""+poi.getDescr()+"\", \"the_geom\" : \""+poi.getPosWKT()+"\", \"foto\" : \""+poi.getRimage()+"\"}]";
		return JPOI;
	}
	
	@Override
	@Transactional
	public PointOfInterest SavePoi(PointOfInterest POI) {
		return Rpoi.save(POI);
	}
	
	@Override
	@Transactional
	public PointOfInterest getPOIbyId(int id) {
		return Rpoi.findById(id);
	}

	@Override
	@Transactional
	public boolean deleteImgPoi(String name) {
		String PhotoPath=".."+File.separator+"CasaPiu"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"POImage"+File.separator;
		boolean removeFileCheck = false;
        try{

            System.out.println("Delete filepath from AJX");
            File file = new File(PhotoPath+name);

            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
                removeFileCheck=true;
            }else{
                System.out.println("Delete operation is failed.");
            }

        }catch(Exception e){

            e.printStackTrace();

        }
		
		return removeFileCheck;
	}
	
	@Override
	@Transactional
	public void dellPoi(PointOfInterest poi) {
		this.deleteImgPoi(poi.getRimage());
		Rpoi.deleteJoinById(poi.getId());
		Rpoi.removeById(poi.getId());
	}
	
	@Override
	public List<PointOfInterest> findAll(){
		return Rpoi.findAll();
	}
	
}
