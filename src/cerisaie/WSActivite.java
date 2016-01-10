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
