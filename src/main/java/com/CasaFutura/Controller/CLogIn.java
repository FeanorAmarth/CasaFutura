package com.CasaFutura.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.CasaFutura.Entity.Ruolo;
import com.CasaFutura.Entity.Utente;
import com.CasaFutura.Service.RuoloService;
import com.CasaFutura.Service.UtenteService;

@Controller
public class CLogIn {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private RuoloService ruoloService;

    @RequestMapping(value = {"home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        return mav;
    }
    
    @RequestMapping(value = {"Admin/home"}, method = RequestMethod.GET)
    public ModelAndView homeAdm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("Admin/home");
        return mav;
    }
    
    @RequestMapping(value = {"login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("home");
        return modelAndView;
    }
    
    @RequestMapping(value="default", method = RequestMethod.GET)
	public ModelAndView defaultUrl(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             ArrayList role = new ArrayList(auth.getAuthorities());
             for (int i=0; i<role.size(); i++){
                 if (Objects.equals(role.get(i).toString(), "ADMIN") == true){
                 modelAndView.setViewName("redirect:Admin/home");
                 }else{
                  modelAndView.setViewName("redirect:home");   
                 }
             }
                 return modelAndView;
        } 
}
