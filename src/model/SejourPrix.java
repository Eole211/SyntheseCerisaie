package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SejourPrix {
	
	@XmlElement(name="sejour")
	Sejour sejour;
	
	@XmlElement(name="prixSejour")
	double prixSejour;
	
	@XmlElement(name="prixActivites")
	double prixActivites;
	
	@XmlElement(name="prixTotal")
	double prixTotal;
	
	public SejourPrix(){
		
	}
	
	public SejourPrix(Sejour s,double  prixSejour,double prixActivites){
		this.sejour=s;
		this.prixSejour=prixSejour;
		this.prixActivites=prixActivites;
	}
	
	
}
