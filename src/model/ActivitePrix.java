package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActivitePrix {
	
	@XmlElement(name="activites")
	List<Activite> activites;
	
	@XmlElement(name="prix")
	double[] prix;
	
	public double getPrixTotal(){
		double totalPrix=0;
		for(double pr :prix){
			totalPrix+=pr;
		}
		return totalPrix;
	}
	
	public ActivitePrix(){
		
	}
	
	public ActivitePrix(List<Activite> acts, double[] prix){
		this.activites=acts;
		this.prix=prix;
	}
	
}
