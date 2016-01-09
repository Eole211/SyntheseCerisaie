package cerisaie;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;

import org.hibernate.*;

import com.sun.jersey.api.JResponse;

import hibernate.HibUtil;
import model.*;
import java.util.*;
import java.text.*;

@Path("/sejour")
public class WSActivite {
	//@Context
	//private UriInfo context;

	/** Creates a new instance of WsSalutation */
	public WSActivite() {
	}
//test g
	
	@GET
	@Path("/getsejour")
	@Produces("application/json")
	public JResponse  getSejourscli(@QueryParam("numcli") int numcli) throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		List<Sejour> sejs =  em.createQuery("from Sejour where client.numCli=:numclip").setParameter("numclip", numcli).getResultList();
		Hibernate.initialize(sejs);
		for(Sejour s :sejs){
			//Suppression des références qui ne nous intéressent pasd
			em.detach(s);
			s.setActivites(null);
			s.getEmplacement().setSejours(null);
			s.getEmplacement().getTypeEmplacement().setEmplacements(null);
			s.getClient().setSejours(null);
		}		
		GenericEntity<List<Sejour>> entity = new GenericEntity<List<Sejour>>(sejs){};
		JResponse r= JResponse.ok(entity).build();
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;		
	}
	

	@GET
	@Path("/getsejours")
	@Produces("application/json")
	// http://localhost:8080/ProjetRestFull/eources/getjson
	public JResponse  getSejours() throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		List<Sejour> sejs =  em.createQuery("from Sejour").getResultList();
		Hibernate.initialize(sejs);
		for(Sejour s :sejs){
			//Suppression des références qui ne nous intéressent pasd
			em.detach(s);
			s.setActivites(null);
			s.getEmplacement().setSejours(null);
			s.getEmplacement().getTypeEmplacement().setEmplacements(null);
			s.getClient().setSejours(null);
		}		
		GenericEntity<List<Sejour>> entity = new GenericEntity<List<Sejour>>(sejs){};
		JResponse r= JResponse.ok(entity).build();
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;		
	}
	
	
	@GET
	@Path("/deletesejour")
	@Produces("text/plain")
	public String deleteSejour(@QueryParam("numsej") int numSej)  throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();
		Sejour sej= em.find(Sejour.class,numSej);
		sej.removeAllActivites();
		em.remove(sej);
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return "ok";
	}
	

	@GET
	@Path("/getactivites")
	@Produces("application/json")
	public JResponse getactivite(@QueryParam("numsej") int numSej)  throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		List<Activite> acts =  em.createQuery("from Activite where sejour.numSej=:numSej").setParameter("numSej", numSej).getResultList();
		Hibernate.initialize(acts);	
		for(Activite a : acts){
			em.detach(a);
			a.setSejour(null);
			a.getSport().setActivites(null);
		}
		GenericEntity<List<Activite>> entity = new GenericEntity<List<Activite>>(acts){};	
		JResponse r= JResponse.ok(entity).build();
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;		
	}
	
	@GET
	@Path("/getprixactivites")
	@Produces("application/json")
	public JResponse getPrixActivite(@QueryParam("numsej") int numSej)  throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		List<Activite> acts =  em.createQuery("from Activite where sejour.numSej=:numSej").setParameter("numSej", numSej).getResultList();
		Hibernate.initialize(acts);	
		List<Double> prix = new LinkedList<>();
		for(Activite a : acts){
			prix.add(new Double(a.getSport().getTarifUnite()*a.getNbloc()));
		}
		GenericEntity<List<Double>> entity = new GenericEntity<List<Double>>(prix){};	
		JResponse r= JResponse.ok(entity).build();
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;		
	}
	

	
	}
