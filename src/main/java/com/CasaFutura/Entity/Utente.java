package com.CasaFutura.Entity;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


@Entity
@Table(name = "utenti")

public class Utente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    
    @Length(min = 9, message = "*Your phone must have at least 9 numbers")
    @NotEmpty(message = "*Please provide your phone")
	private String phone;
    
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    
    @Column(name = "nome")
    @NotEmpty(message = "*Please provide your name")
    private String nome;
    
    @Column(name = "cognome")
    @NotEmpty(message = "*Please provide your last name")
    private String cognome;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "utente_ruolo", joinColumns = @JoinColumn(name = "utente_id"), inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
    private Set<Ruolo> ruoli;
    
    @Autowired
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "uctrl_edu", joinColumns = @JoinColumn(name = "edu_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "uctrl_id", referencedColumnName = "id"))
	private Set<Utente> ctrl;
	
    @Autowired
	@Column(columnDefinition = "geometry")
	private Point pos; 
	
	private long lstccs=0;
	
	private int alert=2;
	
	@Autowired
	@ManyToMany(mappedBy="ctrl", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Utente> edu;
	
	@Autowired
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "uctrl_area", joinColumns = @JoinColumn(name = "uctrl_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "area_id", referencedColumnName = "id"))
	private Set<SafeArea> sarea;
	
	@Autowired
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "uctrl_path", joinColumns = @JoinColumn(name = "uctrl_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "path_id", referencedColumnName = "id"))
	private Set<SafePath> spath;	
	
	public Utente(int id, String nome,String cognome, String password, String email, Set<Ruolo> ruoli, int alert, String posWKT, String phone, long lastAccess) throws ParseException {
		
		setPosWKT("POINT (0 0)");
		setId(id);
		setEmail(email);
		setPassword(password);
		setNome(nome);
		setCognome(cognome);
		setRuoli(ruoli);
		setPhone(phone);
		setLastAccess(lastAccess);
		this.alert=alert;
		this.pos = (Point) new WKTReader().read(posWKT);
		
	}
	
	public Utente(String Name,String Sname, String PassW, String Email, Set<Ruolo> Ruolo, int Alert, String Pos, String Phone) throws ParseException {
		setEmail(Email);
		setPassword(PassW);
		setNome(Name);
		setCognome(Sname);
		setRuoli(Ruolo);
		setPhone(Phone);
		this.alert=Alert;
		this.pos = (Point) new WKTReader().read(Pos);
	}
	
	public long getLastAccess() {
		return this.lstccs;
	}
	
	public void setLastAccess(long LastAccess) {
		this.lstccs = LastAccess;
	}
	
	public void setNowLastAccess() {
		Calendar clndr = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		System.out.println(clndr.getTimeInMillis());
		this.lstccs = clndr.getTimeInMillis();
	}
	
	public int getAlert() {
		return alert;
	}
	
	@JsonIgnore
	public Point getPos() {
		return pos;
	}
	
	public String getPosWKT() {
		if(pos!=null)
			return pos.toString();
		else
			return "POINT(0 0)";
	}
	
	public Set<SafeArea> getSarea(){
		return sarea;
	}

	public Set<SafePath> getSpath(){
		return spath;
	}
	
	public void setAlert(int alert) {
		this.alert=alert;
	}
	
	public void setPosWKT(String poss) throws ParseException {
		pos= (Point) new WKTReader().read(poss);
	}
	
	@JsonIgnore
	public void setPos(Point poss) {
		pos=poss;
	}
	
	public void setEdu(Set<Utente>  Edu) {
		this.edu = Edu;
	}
	
	public void setSarea(Set<SafeArea> SArea) {
		this.sarea = SArea;
	}
	
	public void setSpath(Set<SafePath> SPath) {
		this.spath = SPath;
	}
	
	public void addEdu(Utente Edu) {
		this.edu.add(Edu);
	}
	
	public void addSArea(SafeArea SArea) {
		this.sarea.add(SArea);
	}
	
	public void addSPath(SafePath SPath) {
		this.spath.add(SPath);
	}
    
    public Utente() {
    
    }
    
    public Utente(int id, String email, String Phone, String password, String nome, String cognome, Set<Ruolo> ruoli) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruoli = ruoli;
        this.phone = Phone;
    }
    
    public Set<Utente> getCtrl(){
		return ctrl;
	}
	
	public void setCtrl(Set<Utente> Ctrl) {
		this.ctrl = Ctrl;
	}
	
	public void addCtrl(Utente Ctrl) {
		this.ctrl.add(Ctrl);
	}
	
	public void popCtrl(Utente Ctrl) {
		this.ctrl.remove(Ctrl);
	}
    
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone=phone;
	}    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public Set<Ruolo> getRuoli() {
        return ruoli;
    }
    
    public void setRuoli(Set<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        hash = 59 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utente other = (Utente) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }
    
    
    
}
