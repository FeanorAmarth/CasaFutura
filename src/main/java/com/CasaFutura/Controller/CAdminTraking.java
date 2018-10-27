/*prima bozza, incompeta del controller per la gestione degli utenti controllati.
 * al momento vengono gestiti solo un paio di casi, restituendo solo una scarna interfaccia.
 * ovviemente l'idea è di estenderla ed uniformarla in maniera coerente con il resto del progetto*/

package com.CasaFutura.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.CasaFutura.Entity.PointOfInterest;
import com.CasaFutura.Entity.SafeArea;
import com.CasaFutura.Entity.SafePath;
import com.CasaFutura.Entity.Utente;
import com.CasaFutura.Repository.RPointOfInterest;
import com.CasaFutura.Repository.RSafeArea;
import com.CasaFutura.Repository.RSafePath;
import com.CasaFutura.Repository.RUtente;
import com.CasaFutura.Service.PointOfInterestService;
import com.CasaFutura.Service.SafeAreaService;
import com.CasaFutura.Service.SafePathService;
import com.CasaFutura.Service.UtenteCtrlService;
import com.CasaFutura.Service.UtenteService;
import com.vividsolutions.jts.io.ParseException;

@Controller
@RequestMapping("/Admin/**")
public class CAdminTraking {
	
	@Autowired
	private PointOfInterestService POIservice;
	
	@Autowired
	private UtenteCtrlService UsrCtrlservice;
	
	@Autowired
	private SafePathService SfPthService;
	
	@Autowired
	private SafeAreaService SfArService;
	
	@Autowired
	private UtenteService UtntService;
	
	@GetMapping(path="CtrlUser")
	public ModelAndView homeAdmnCtrlUs() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/Admin/MonitorCtrlUsr");
		return modelAndView;
	}
	
	@GetMapping(path="CtrlParents")
	public ModelAndView homeAdmnCtrlPrnts() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/Admin/CtrlParents");
		return modelAndView;
	}
	
	@GetMapping(path="WatchCtrlUsr")
	public ModelAndView homeAdmnWiev() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/Admin/WatchCtrlUsr");
		return modelAndView;
	}

	@GetMapping(path="delnewPoi", params  = {"id"})
	public @ResponseBody void  delnewPoi(@RequestParam("id") int id) {
		PointOfInterest poi = POIservice.getPOIbyId(id);
		POIservice.dellPoi(poi);
	}
	
	@PostMapping(path="saveNewPoi")
	public @ResponseBody String saveNewPoi(@RequestParam("img") MultipartFile photo, @RequestParam("posWKT") String posWKT, @RequestParam("descr") String descr ) throws ParseException {
		String MCMDF=".."+File.separator+"CasaPiu"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"POImage"+File.separator;
		//String MCMDF="C:\\apache-tomcat-9.0.10\\webapps\\CasaFutura\\WEB-INF\\classes\\static\\POImage";
		String response="{\"status\" : ";
		if(!photo.isEmpty()) {
			try {
				String imname=photo.getOriginalFilename();
//				File PhotoPoi= new File( PhotoPath + imname );
//				BufferedOutputStream stream = new BufferedOutputStream ( new FileOutputStream(PhotoPoi) );
//				stream.write(file.getBytes());
//				stream.close();
				
				byte[] bytes = photo.getBytes();
	            Path path = Paths.get(MCMDF + photo.getOriginalFilename());
	            Files.write(path, bytes);
				
				PointOfInterest poi = new PointOfInterest();
				poi.setPosWKT(posWKT);
				poi.setDescr(descr);
				poi.setRimage(imname);
				poi=POIservice.SavePoi(poi);
				response+="0, \"imgname\" : \""+imname+"\", \"id\" : "+poi.getId()+"}";
			}catch (IOException e) {
				response+=e.getLocalizedMessage()+"}";
				e.printStackTrace();
			}
		}else{
			response="2}";
		}
		return response;
	}
	
	@PostMapping(path="modNewPoi")
	public @ResponseBody String modNewPoi( @RequestParam("posWKT") String posWKT, @RequestParam("descr") String descr, @RequestParam("id") int id) throws ParseException {
		
		String response="{\"status\" : ";
		PointOfInterest poi = POIservice.getPOIbyId(id);
		poi.setPosWKT(posWKT);
		poi.setDescr(descr);
		poi=POIservice.SavePoi(poi);
		response+="0, \"imgname\" : \""+poi.getRimage()+"\", \"id\" : "+poi.getId()+"}";

		return response;
		
	}
	
	@GetMapping(path="getMySons", produces="application/json")
	public @ResponseBody Set<Utente> whoYouAre(@RequestParam("id") int id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentMail = authentication.getName();
		return UtntService.findUtenteById(id).getCtrl();
	}
	
	@GetMapping(params  = {"task=getAllCtrlUsr"}, produces="application/json")
	public @ResponseBody Set<Utente> whoYouAre(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentMail = authentication.getName();
		return UtntService.findUtenteByEmail(currentMail).getCtrl();
	}
	
	@GetMapping(path="pathlessPoi", produces="application/json")
	public @ResponseBody List<PointOfInterest> gatPathlessPoi(){
		return POIservice.findAll();
	}
	
	@PostMapping(path="saveGeoCng", consumes="application/json")
	public @ResponseBody String saveGeoCng(@RequestBody Utente ctrlusr){
		
		String ret="non è stato possibile concludere il salvataggio, l'utente selezionato non esiste.";
		if(ctrlusr!=null) {
			UtntService.updateUtente(ctrlusr);
			ret="modifiche salvate con successo!";
		}
		
		return ret;
		
	}
	
	@PostMapping(path="saveGeoCngPoi", consumes="application/json")
	public @ResponseBody String saveGeoCngPoi(@RequestBody List<PointOfInterest> pois){
		
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		if(pois!=null) {
			pois.forEach( poi -> {
				POIservice.SavePoi(poi);
			});
			ret="";
		}
		
		return ret;

	}
	
	@GetMapping(path="delPath", params  = {"id"})
	public @ResponseBody String delPath(@RequestParam("id") int id) {
		System.out.println(id);
		SafePath path = SfPthService.findById(id);
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		
		if(path!=null) {
			SfPthService.delSafePathById(id);
			ret="";
		}
		
		return ret;

	}
	
	@GetMapping(path="delArea", params  = {"id"})
	public @ResponseBody String delArea(@RequestParam("id") int id) {
		
		SafeArea area = SfArService.findById(id);
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		if(area!=null) {
			SfArService.dellSafePathById(id);
			ret="";
		}
		
		return ret;

	}
	
	@GetMapping(path="getUnCtrlUs")
	public @ResponseBody List<Utente> getUnCtrlUs() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentMail = authentication.getName();
		List<Utente> unUs=UtntService.findUnCtrl(UtntService.findUtenteByEmail(currentMail).getId());

		if(unUs.isEmpty()) {
			if(UtntService.isEmptMntrUtCtrl()==0) {
				unUs=UtntService.findUnCtrlAll();
			}
		}
		return unUs;
	}
	
	@GetMapping(path="getCtrlUs")
	public @ResponseBody List<Utente> getUnCtrlUs(@RequestParam("id") int id) {
		
		List<Utente> unUs=UtntService.findUnCtrl(id);
		if(unUs.isEmpty()) {
			if(UtntService.isEmptMntrUtCtrl()==0) {
				unUs=UtntService.findUnCtrlAll();
			}
		}
		
		return unUs;
	}
	
	@GetMapping(path="getParents")
	public @ResponseBody List<Utente> getParents() {
		return UtntService.findParentAll();
	}
	
	@PostMapping(path="remCtrlUs", params  = {"id"})
	public @ResponseBody String remCtrlUs(@RequestParam("id") int id) {
		
		Utente urs=UtntService.findUtenteById(id);
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		if(urs!=null) {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentMail = authentication.getName();
			Utente Edu = UtntService.findUtenteByEmail(currentMail);
			Edu.popCtrl(urs);
			UtntService.updateUtente(Edu);
			ret="";
		}
		
		return ret;
	}
	
	@PostMapping(path="remCtrlSon", params  = {"id", "eId"})
	public @ResponseBody String remCtrlUs(@RequestParam("id") int id, @RequestParam("eId") int eId) {
		
		Utente urs=UtntService.findUtenteById(id);
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		if(urs!=null) {
			Utente Edu = UtntService.findUtenteById(eId);
			Edu.popCtrl(urs);
			UtntService.updateUtente(Edu);
			ret="";
		}
		
		return ret;
	}
	
	@PostMapping(path="addNewCrtlUsr")
	public @ResponseBody String saveGeoCng(@RequestParam("id") int id){
		
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		
		Utente urs=UtntService.findUtenteById(id);
		
		if(urs!=null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentMail = authentication.getName();
			Utente Edu = UtntService.findUtenteByEmail(currentMail);
			Edu.addCtrl(urs);
			UtntService.updateUtente(Edu);
			ret="";
		}
		
		return ret;

	}
	
	@PostMapping(path="addNewSon")
	public @ResponseBody String saveGeoCng(@RequestParam("id") int id, @RequestParam("eId") int eId){
		
		String ret="Opss... c'e' stato un errore, operazione non completata con successo.";
		
		Utente urs=UtntService.findUtenteById(id);
		
		if(urs!=null) {
			Utente Edu = UtntService.findUtenteById(eId);
			Edu.addCtrl(urs);
			UtntService.updateUtente(Edu);
			ret="";
		}
		
		return ret;

	}
	
}
