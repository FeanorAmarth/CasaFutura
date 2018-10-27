package com.CasaFutura.Repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.CasaFutura.Entity.Ruolo;


public interface RRuolo extends CrudRepository<Ruolo, Integer>{
    
    Ruolo findByRuolo(String ruolo);
    
    Set<Ruolo> findAll();
    
}
