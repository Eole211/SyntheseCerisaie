package cerisaie;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.hibernate.*;

import com.sun.jersey.api.JResponse;

import hibernate.HibUtil;
import model.*;
import java.util.*;
import java.text.*;

@Path("/sejour")
public class WSSejour {
	//@Context
	//private UriInfo context;

	/** Creates a new instance of WsSalutation */
	public WSSejour() {
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
		List<SejourPrix> sejPs=new LinkedList<>();
		double prixSej,prixActs;
		for(Sejour s :sejs){
			//Suppression des références qui ne nous intéressent pasd
			em.detach(s);
			//Calcul des prix
			prixSej=s.generatePrice();
			prixActs=Sejour.getActivitesPrix(s.getNumSej(),em).getPrixTotal();
			s.setActivites(null);
			s.getEmplacement().setSejours(null);
			s.getEmplacement().getTypeEmplacement().setEmplacements(null);
			s.getClient().setSejours(null);
			sejPs.add(new SejourPrix(s,prixSej,prixActs));
		}		
		GenericEntity<List<SejourPrix>> entity = new GenericEntity<List<SejourPrix>>(sejPs){};
		JResponse r= JResponse.ok(entity).build();
	
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;		
	}
	
	
	@GET
	@Path("/getsejourfromid")
	@Produces("application/json")
	public JResponse  getSejourFromId(@QueryParam("numsej") int numsej) throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		try{
		Sejour sej =  em.find(Sejour.class,numsej);
		Hibernate.initialize(sej);
	   SejourPrix sejPs;
		double prixSej,prixActs;
		Sejour s= sej;
			//Suppression des références qui ne nous intéressent pasd
			em.detach(s);
			//Calcul des prix
			prixSej=s.generatePrice();
			prixActs=Sejour.getActivitesPrix(s.getNumSej(),em).getPrixTotal();
			s.setActivites(null);
			s.getEmplacement().setSejours(null);
			s.getEmplacement().getTypeEmplacement().setEmplacements(null);
			s.getClient().setSejours(null);
			sejPs=new SejourPrix(s,prixSej,prixActs);
			
		GenericEntity<SejourPrix> entity = new GenericEntity<SejourPrix>(sejPs){};
		JResponse r= JResponse.ok(entity).build();
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;	
		}
		catch(Exception e){
			//em.getTransaction().commit();
			HibUtil.closeEntityManager();
			return null;
		}
		
		
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
	

	@POST
	@Path("/createsejour")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces("text/plain")
	public String createSejour(SejourJson sejParam){
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		Sejour s=sejParam.toSejour(em);
		em.persist(s);
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return "ok";		
	}
	
	
	
	@GET
	@Path("/getactivites")
	@Produces("application/json")
	public JResponse getactivite(@QueryParam("numsej") int numSej)  throws ParseException {
			ActivitePrix ap = Sejour.getActivitesPrix(numSej);
			GenericEntity<ActivitePrix> entity = new GenericEntity<ActivitePrix>(ap){};	
			JResponse r= JResponse.ok(entity).build();
			return r;
		}
	
	

	}
