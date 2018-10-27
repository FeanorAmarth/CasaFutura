package com.CasaFutura.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.Ruolo;
import com.CasaFutura.Repository.RRuolo;

@Service("RuoloService")
public class RuoloServiceImpl implements RuoloService{
    
    
    @Autowired
    private RRuolo ruoloRepository;

    @Override
    public Set<Ruolo> getAllRuolo() {
        return ruoloRepository.findAll() ;
        
    }
    
}
