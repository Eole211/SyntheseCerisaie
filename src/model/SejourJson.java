package model;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SejourJson {
	

	@XmlElement
	private Date datedebSej;

	@XmlElement
	private Date dateFinSej;

	@XmlElement
	private int nbPersonnes;

	@XmlElement
	private int numClient;

	@XmlElement
	private int numEmplacement;

	
	public SejourJson(){
		
	}
	
	public Sejour toSejour(EntityManager em){
		Sejour sej= new Sejour();
		sej.setClient(em.find(Client.class,numClient));
		sej.setDatedebSej(datedebSej);
		sej.setDateFinSej(dateFinSej);
		sej.setNbPersonnes(nbPersonnes);
		sej.setEmplacement(em.find(Emplacement.class, numEmplacement));
		return sej;
	}
	
	
}
