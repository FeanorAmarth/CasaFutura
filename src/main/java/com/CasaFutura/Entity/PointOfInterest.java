package com.CasaFutura.Entity;

import javax.persistence.*;

import org.geolatte.geom.codec.Wkt;
import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/* potrebbero mancare alcuni parametri non stettamente necessari alla gestione della posizione.*/

@Entity
@Table(name = "PointOfInterest")
public class PointOfInterest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false, unique = true)
	private int id;
	
	@Autowired
	@Column(columnDefinition = "geometry")
	private Point pos;
	
	private String descr;
	
	private String rimage;
	
	public PointOfInterest() {
		
	}
	
	public PointOfInterest (double lat, double lon, String imname) throws ParseException {
		this.pos = (Point) new WKTReader().read("POINT("+lat+" "+lon+")");
		this.descr="Default Description";
		this.rimage=imname;
	}
	
	public void setId(int ids) {
		id=ids;
	}
	
	public void setPosWKT(String poss) throws ParseException {
		pos= (Point) new WKTReader().read(poss);
	}
	
	public void setPos(Point poss) {
		pos=poss;
	}
	
	public void setDescr(String descrs) {
		descr=descrs;
	}
	
	public void setRimage(String rimages) {
		rimage=rimages;
	}
	
	
	public int getId() {
		return id;
	}
	
	@JsonIgnore
	public Point getPos() {
		return pos;
	}
	
	public String getPosWKT() {
		return pos.toString();
	}
	
	
	public String getDescr() {
		return descr;
	}
	
	public String getRimage() {
		return rimage;
	}
	
}
