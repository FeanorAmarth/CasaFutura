package com.CasaFutura.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/* potrebbero mancare alcuni parametri non stettamente necessari alla gestione della posizione.*/

@Entity
@Table(name = "SafeArea")
public class SafeArea {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Autowired
	@Column(columnDefinition = "geometry")
	private Polygon sarea;
	
	private String name;
	
	public SafeArea() {
		
	}
	
	public int getid() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@JsonIgnore
	public Polygon getSArea() {
		return sarea;
	}
	
	public String getSAreaWKT() {
		return sarea.toString();
	}
	
	public void setId(int Id) {
		this.id=Id;
	}
	
	@JsonSetter("nome")
	public void setName(String Name) {
		this.name=Name;
	}
	
	@JsonSetter("sareaWKT")
	public void setSArea(String Area) throws ParseException {
		this.sarea=(Polygon) new WKTReader().read(Area);
	}
	
	@JsonIgnore
	public void setSArea(Polygon Area) {
		this.sarea=Area;
	}
	
}
