/* Prima Bozza del Controller, che reimplementa l'interfaccia offerta al client Android.
 * Al momento vengono gestite le seguenti attivita' dell'app;
 * - Navigazione Libera
 * - Navigazione sul Percorso
 * - Creazione di un POI durante la navigazione
 * ho cercato di mantenere invariate la forma dei JSON di risposta, e sembra che l'app
 * riesca ad interfacciarsi con questo server senza grossi cambiamenti....*/

package com.CasaFutura.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.CasaFutura.Entity.PointOfInterest;
import com.CasaFutura.Entity.Ruolo;
import com.CasaFutura.Entity.SafePath;
import com.CasaFutura.Entity.Utente;
import com.CasaFutura.Repository.RUtente;
import com.CasaFutura.Service.PointOfInterestService;
import com.CasaFutura.Service.SafeAreaService;
import com.CasaFutura.Service.SafePathService;
import com.CasaFutura.Service.UtenteCtrlService;
import com.CasaFutura.Service.UtenteService;
import com.vividsolutions.jts.io.ParseException;

@Controller
@RequestMapping("/OutDoorTracking/*")
public class COutDoorTracking {
	
	@Autowired
	private UtenteCtrlService UCservice;
	
	@Autowired
	private UtenteService Uservice;
	
	@Autowired
	private SafePathService SPservice;
	
	@Autowired
	private SafeAreaService SAservice;
	
	@Autowired
	private PointOfInterestService POIservice;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@GetMapping(params = {"task=SetStndrUsr"})
	public @ResponseBody void SetStndrUsr() throws ParseException {
		
		Utente edu = new Utente();
		
//		Ruolo ruolo1 = new Ruolo();
//		Ruolo ruolo2 = new Ruolo();
//		ruolo1.setRuolo("GENITORE");
//		ruolo1.setId(3);
//		ruolo2.setRuolo("USER");
//		ruolo2.setId(4)
//		
//		SafeArea sarea = new SafeArea();
//		sarea.setName("AreaTest");
//		sarea.setSArea("POLYGON((42.5456850301882 14.1110305664395,42.5455012538801 14.1107274768207,42.5453629272698 14.1109179136607,42.5455536203026 14.1111807701442,42.5456850301882 14.1110305664395,42.5456850301882 14.1110305664395))");
//		
//		SafePath spath = new SafePath();
//		spath.setName("PathTest");
//		spath.setDuration(5);
//		spath.setSpath("LINESTRING(42.5456309512623 14.111135172591,42.5461012584674 14.1106201884602,42.5463028176136 14.111119079337,42.5456230469092 14.1118325469349,42.5455598120488 14.1115857837056,42.5457376599305 14.1113443848942)");
//		
//		UtenteCtrl ctrl = new UtenteCtrl();
//		ctrl.setNome("Pippo");
//		ctrl.setCognome("User");
//		ctrl.setEmail("pippouser@mail.com");
//		ctrl.setPassword(bCryptPasswordEncoder.encode("pippo"));
//		ctrl.setRuoli(new HashSet<Ruolo>());
//		ctrl.getRuoli().add(ruolo2);
//		ctrl.setPhone("3292954949");
//		ctrl.setAlert(0);
//		ctrl.setPosWKT("POINT(0 0)");
//		ctrl.setSarea(new HashSet<SafeArea>());
//		ctrl.addSArea(sarea);
//		ctrl.setSpath(new HashSet<SafePath>());
//		ctrl.addSPath(spath);
//
		edu.setNome("Topolino");
		edu.setCognome("Genitore");
		edu.setEmail("genitore@mail.com");
		edu.setPassword("genitore");
		edu.setPosWKT("POINT(0 0)");
//		edu.setRuoli(new HashSet<Ruolo>());
//		edu.getRuoli().add(ruolo1);
		edu.setPhone("CALL-GENITORE");
//		edu.setCtrl(new HashSet<UtenteCtrl>());
//		edu.addCtrl(ctrl);
		Uservice.saveUtente(edu);
	}
	
	/*controlla che l'utente identificato dal numero di telefono "phone",
	 * sia all'intero di una delle sue aree sicure durante la navigazione libera.
	 * Restitusce un JSON che notifica il suo stato;
	 * -alert : 0 e' al sicuro (nessun allerme)
	 * -alert : 1 e' all'esterno di tutte le aree a lui associate (induce allarme nel dispositivo mobile)*/
	
	@GetMapping(params  = {"task=monitor" , "phone", "lat", "lon"}, produces="application/json")
	public @ResponseBody String whereAreYou(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("phone") String  phone) throws ParseException{
		
		String PosWKT="POINT("+lat+" "+lon+")";
		Utente imctrl=UCservice.GetUserCtrlByPhone(phone);
		imctrl.getRuoli().contains(new Ruolo(4,"UTENTE"));
		String Res;
		
		if(imctrl!=null && imctrl.getRuoli().contains(new Ruolo(4,"UTENTE"))) {
			if(SAservice.IsInSArea(UCservice.GetMultiPolyWKT(imctrl), PosWKT)==1) {
				imctrl.setAlert(0);
				System.out.println("in SArea");
			}else {
				imctrl.setAlert(1);
				System.out.println("out SArea");
			}
			imctrl.setPosWKT(PosWKT);
			imctrl.setNowLastAccess();
			UCservice.SaveUserCtrl(imctrl);
			Res="{ \"alert\" : "+imctrl.getAlert()+" }";
		}else {
			System.out.println("no result");
			Res="{ \"alert\" : 1 }";
		}
		
		return Res;
	}
	
	/* controlla che l'utente identificato dal numero di telefono "phone"
	 * segua il suo percorso sicuro (id), ed aggiorna il POI mostrato.
	 * Restitusce un JSON che notifica il suo stato, ed il POI piu vicino all'attuale posizione dell'utente;
	 * -alert : come sopra
	 * -pois: attributi del POI piu' vicino alla posizione dell'utente lungo il percorso*/
	
	@GetMapping(params  = {"task=navigazione", "id" , "phone", "lat", "lon"}, produces="application/json")
	public @ResponseBody String whereAreYou(@RequestParam("id") int id, @RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("phone") String  phone) throws ParseException{
		
		String Pos="POINT("+lat+" "+lon+")";
		String Res="{";
		Utente imctrl=UCservice.GetUserCtrlByPhone(phone);
		
		if(imctrl!=null && imctrl.getRuoli().contains(new Ruolo(4,"UTENTE"))) {
			
			SafePath imsp=UCservice.GetMySPathById(imctrl, id);
			String JPOI="1";
			Res+="\"alert\" : "+imctrl.getAlert();
			if(SPservice.IsInSpath( imsp.getSPath().toString(), Pos)==1) {
				imctrl.setAlert(0);
				JPOI=POIservice.getNextPoiJson(id, Pos);
				Res+=", \"pois\" : "+JPOI+"}";
				System.out.println("in Spath");
			}else {
				imctrl.setAlert(1);
				Res+="}";
				System.out.println("out Spath");
			}
			imctrl.setPosWKT(Pos);
			imctrl.setNowLastAccess();
			UCservice.SaveUserCtrl(imctrl);
			
			
		}else {
			System.out.println("no result");
			Res+="\"alert\" : 1}";
		}
		
		return Res;
	}
	
	/*restituisce un JSON con la lista dei percorsi associati ad un utente*/
	
	@GetMapping(params  = {"task=get_percorsi", "phone"}, produces="application/json")
	public @ResponseBody Set<SafePath> WhatAreYourPaths(@RequestParam("phone") String  phone){
		
		Utente imctrl=UCservice.GetUserCtrlByPhone(phone);
		
		return imctrl.getSpath();
	}
	
	/* Gestice l'upload di una foto dal dispositivo durante la navigazione (sia libera che in percorso).
	 * in caso sia necessario è possibile far associare automaticamente il poi al percorso dell'utente che disti di meno
	 * dal poi stesso. ma questa funzione non è stata implementata perche' non specificata nela versione precedente*/

	@PostMapping(params = {"lat","lon"})
	public @ResponseBody String HandlePoiPhoto(@RequestParam("photo") MultipartFile photo, @RequestParam("lat") double lat, @RequestParam("lon") double lon){
		
		String MCMDF=".."+File.separator+"CasaPiu"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"POImage"+File.separator;
		//String MCMDF="C:\\apache-tomcat-9.0.10\\webapps\\CasaFutura\\WEB-INF\\classes\\static\\POImage";
		String response ="{\"photo\" : ";
		
		if(!photo.isEmpty()) {
			try {
				String imname=photo.getOriginalFilename();
//				File PhotoPoi= new File( PhotoPath + imname );
//				BufferedOutputStream stream = new BufferedOutputStream ( new FileOutputStream(PhotoPoi) );
//				stream.write(photo.getBytes());
//				stream.close();
				
				byte[] bytes = photo.getBytes();
	            Path path = Paths.get(MCMDF + photo.getOriginalFilename());
	            Files.write(path, bytes);
				
				response+=0;
				
				PointOfInterest poi = new PointOfInterest(lat, lon, imname);
				POIservice.SavePoi(poi);
				
			}catch (Exception e) {
				response+=1;
			}
		}else{
			response+=1;
		}
		
		System.out.println(response+"}");
		return response+="}";
	}
	
	@GetMapping(path="PrntWatchCtrlUsr")
	public ModelAndView homeAdmnWiev() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			modelAndView.setViewName("/PrntWatchCtrlUsr");
		}else {
			modelAndView.setViewName("redirect:/home");
		}
		return modelAndView;
	}
	
	@GetMapping(path="getAllCtrlSons", produces="application/json")
	public @ResponseBody Set<Utente> whoYouAre(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentMail = authentication.getName();
			Utente ut=Uservice.findUtenteByEmail(currentMail);
			if(ut.getRuoli().contains(new Ruolo(3,"GENITORE")))
				return ut.getCtrl();
		}
		return null;
	}
	
}