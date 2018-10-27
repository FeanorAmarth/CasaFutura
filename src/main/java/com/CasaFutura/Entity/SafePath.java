package com.CasaFutura.Entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/* potrebbero mancare alcuni parametri non stettamente necessari alla gestione della posizione, 
 * ci potrebbero essere metodi get e set ridondanti a causa delle prove per la generazione dei JSON... sarà riordinato.*/

@Entity
@Table(name = "SafePath")
public class SafePath {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Autowired
	@Column(columnDefinition = "geometry")
	private LineString spath;
	
	private int duration;
	
	private String name;
	
	@Autowired
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
				name = "spath_poi", 
				joinColumns = @JoinColumn(name = "spath_id", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "poi_id", referencedColumnName = "id")
				)
	
	private Set<PointOfInterest> poi;
	
	public SafePath() {
		
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@JsonSetter("nome")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonSetter("tempo")
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setSpath(LineString spath) {
		this.spath = spath;
	}
	
	@JsonSetter("poi")
	public void setPoi(Set<PointOfInterest> poi) {
		this.poi = poi;
	}
	
	public void addPoi(PointOfInterest Poi) {
		this.poi.add(Poi);
	}
	
	@JsonSetter("the_geom")
	public void setSpath(String spath) throws ParseException {
		this.spath=(LineString) new WKTReader().read(spath);
	}
	
	public int getId() {
		return id;
	}
	
	@JsonIgnore
	public int getDuration() {
		return duration;
	}
	
	@JsonIgnore
	public LineString getSPath() {
		return spath;
	}
	
	@JsonIgnore
	public String getName() {
		return name;
	}
	
	public String getNome() {
		return name;
	}
	
	public int getTempo() {
		return duration;
	}
	
	public String getThe_geom() {
		return spath.toString();
	}
	
	public Set<PointOfInterest> getPoi(){
		return poi;
	}
	
}
