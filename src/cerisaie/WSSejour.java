package cerisaie;

import javax.persistence.*;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.JResponse;
import com.sun.jersey.api.JResponse.JResponseBuilder;

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
	

	
	}
