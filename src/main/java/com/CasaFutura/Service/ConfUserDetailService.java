package com.CasaFutura.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.CasaFutura.Entity.Ruolo;
import com.CasaFutura.Entity.Utente;
import com.CasaFutura.Repository.RUtente;

@Service
public class ConfUserDetailService implements UserDetailsService {

    @Autowired
    private RUtente utenteRepository;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email);

        if (utente == null) {
            throw new UsernameNotFoundException("email not found");

        }
        
        //utente.setPassword(bCryptPasswordEncoder.encode(utente.getPassword()));
        //utenteRepository.save(utente);
        
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Ruolo ruolo : utente.getRuoli()) {
            System.out.println("it.casafutura.tesi.serviceImpl.ConfugurazioneUserDetailService.loadUserByUsername() " + ruolo.getRuolo());
            grantedAuthorities.add(new SimpleGrantedAuthority(ruolo.getRuolo()));
        }

        return new org.springframework.security.core.userdetails.User(
                email,
                utente.getPassword(),
                grantedAuthorities);
        //      , Collections.singleton(new SimpleGrantedAuthority("utente")));
    }

}
