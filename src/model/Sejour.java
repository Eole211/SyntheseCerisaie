package model;

import java.io.Serializable;
import javax.persistence.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Hibernate;

import hibernate.HibUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * The persistent class for the sejour database table.
 * 
 */
@Entity
@NamedQuery(name="Sejour.findAll", query="SELECT s FROM Sejour s")
@XmlRootElement
public class Sejour implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int numSej;

	@Temporal(TemporalType.DATE)
	private Date datedebSej;

	@Temporal(TemporalType.DATE)
	private Date dateFinSej;

	private int nbPersonnes;

	//bi-directional many-to-one association to Activite
	@OneToMany( mappedBy="sejour")
	private List<Activite> activites;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="NumCli")
	private Client client;

	//bi-directional many-to-one association to Emplacement
	@ManyToOne
	@JoinColumn(name="NumEmpl")
	private Emplacement emplacement;

	public Sejour() {
	}

	public int getNumSej() {
		return this.numSej;
	}

	public void setNumSej(int numSej) {
		this.numSej = numSej;
	}

	public Date getDatedebSej() {
		return this.datedebSej;
	}

	public void setDatedebSej(Date datedebSej) {
		this.datedebSej = datedebSej;
	}

	public Date getDateFinSej() {
		return this.dateFinSej;
	}

	public void setDateFinSej(Date dateFinSej) {
		this.dateFinSej = dateFinSej;
	}

	public int getNbPersonnes() {
		return this.nbPersonnes;
	}

	public void setNbPersonnes(int nbPersonnes) {
		this.nbPersonnes = nbPersonnes;
	}

	public List<Activite> getActivites() {
		return this.activites;
	}

	public void setActivites(List<Activite> activites) {
		this.activites = activites;
	}

	public Activite addActivite(Activite activite) {
		getActivites().add(activite);
		activite.setSejour(this);

		return activite;
	}

	public Activite removeActivite(Activite activite) {
		getActivites().remove(activite);
		activite.setSejour(null);

		return activite;
	}
	
	public void removeAllActivites() {
		EntityManager em=HibUtil.getEntityManager();
		Query q = em.createNativeQuery("DELETE FROM activite where NumSej= ?1");
		q.setParameter(1, this.numSej);
		q.executeUpdate();	
		
	}
	
	public double generatePrice(){
		Sejour s= this;
		int dayBeg, dayEnd;
		Calendar cal= Calendar.getInstance();
		cal.setTime(s.getDateFinSej());
		dayEnd=cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(s.getDatedebSej());
		dayBeg=cal.get(Calendar.DAY_OF_YEAR);
		if(dayEnd<dayBeg){
			dayEnd+=cal.getActualMaximum(Calendar.DAY_OF_YEAR);
		}
		return (dayEnd-dayBeg)*s.getEmplacement().getTypeEmplacement().getTariftypepl();
	}
	

	public static ActivitePrix getActivitesPrix(int numSej){
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		ActivitePrix ap=getActivitesPrix(numSej,em);
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return ap;
		
	}
	public static ActivitePrix getActivitesPrix(int numSej, EntityManager em){
		List<Activite> acts =  em.createQuery("from Activite where sejour.numSej=:numSej").setParameter("numSej", numSej).getResultList();
		Hibernate.initialize(acts);	
		List<Object> json= new LinkedList<>();
		double[] prix =new double[acts.size()];
		int i=0;
		for(Activite a : acts){
			prix[i]=a.getSport().getTarifUnite()*a.getNbloc();
			em.detach(a);
			a.setSejour(null);
			a.getSport().setActivites(null);
			i++;
		}
		ActivitePrix ap=new ActivitePrix(acts,prix);
		return ap;
	}
	
	
	
	public double generateActivitiesPrice(){
		double prix=0;
		for(Activite act: this.activites)
		{
			prix+=act.getNbloc()*act.getSport().getTarifUnite();
		}
		return prix;
	}
	
	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Emplacement getEmplacement() {
		return this.emplacement;
	}

	public void setEmplacement(Emplacement emplacement) {
		this.emplacement = emplacement;
	}

}