package model;

import java.util.Date;
import java.util.List;
import java.util.Random;

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
	private int numCli;

	@XmlElement
	private int numEmplacement;

	
	public SejourJson(){
		
	}
	
	public Sejour toSejour(EntityManager em){	
		Sejour sej= new Sejour();
		List<Sejour> sejs =  em.createQuery("from Sejour").getResultList();
		int maxId=0;
		for(Sejour s :sejs){
			if(s.getNumSej()>maxId)
				maxId=s.getNumSej();
		}
		maxId++;
		//em.persist(sej);
		sej.setClient(em.find(Client.class,numCli));
		sej.setDatedebSej(datedebSej);
		sej.setDateFinSej(dateFinSej);
		sej.setNbPersonnes(nbPersonnes);
		sej.setEmplacement(em.find(Emplacement.class, numEmplacement));
		sej.setNumSej(maxId);//new Random().nextInt(9000));
		return sej;
	}
	
	
}
