package cerisaie;

import javax.persistence.*;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
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

@Path("/client")
public class WSClient {	
	//@Context
	//private UriInfo context;

	/** Creates a new instance of WsSalutation */
	public WSClient() {
	}

	


	@GET
	@Path("/getclient")
	@Produces("application/json; charset=utf-8")
	// http://localhost:8080/ProjetRestFull/eources/getjson
	public JResponse  getEtudiant(@QueryParam("numcli") int numCli) throws ParseException {
	try{
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		Client c=  em.find(Client.class, numCli);
		Hibernate.initialize(c);
		
			//On met le séjour à null, sinon il y a des références circulaires.
			em.detach(c);
			c.setSejours(null);
				
		GenericEntity<Client> entity = new GenericEntity<Client>(c) {};
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
	@Path("/getclients")
	@Produces("application/json; charset=utf-8")
	// http://localhost:8080/ProjetRestFull/eources/getjson
	public JResponse  getEtudiantToJSONText() throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		List<Client> clis =  em.createQuery("from Client").getResultList();
		Hibernate.initialize(clis);
		for(Client c :clis){
			//On met le séjour à null, sinon il y a des références circulaires.
			em.detach(c);
			c.setSejours(null);
		}		
		GenericEntity<List<Client>> entity = new GenericEntity<List<Client>>(clis) {};
		JResponse r= JResponse.ok(entity).build();
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return r;		
	}
	
	
	
	@POST
	@Path("createclient")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces("text/plain")
	public String createClient(Client cliParam){
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		em.persist(cliParam);
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return "ok";		
	}
	
	
	@POST
	@Path("updateclient")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces("text/plain")
	public String updateClient(Client cliParam){
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		Client cli= em.find(Client.class,cliParam.getNumCli());
		if(cliParam.getAdrRueCli()!=null){
			cli.setAdrRueCli(cliParam.getAdrRueCli());			
		}
		if(cliParam.getNomCli()!=null){
			cli.setNomCli(cliParam.getNomCli());
		}
		if(cliParam.getVilleCli()!=null){
			cli.setVilleCli(cliParam.getVilleCli());
		}
		if(cliParam.getAdrRueCli()!=null){
			cli.setAdrRueCli(cliParam.getAdrRueCli());
		}
		if(cliParam.getCpCli()!=null){
			cli.setCpCli(cliParam.getCpCli());
		}
		if(cliParam.getNumPieceCli()!=null){
			cli.setNumPieceCli(cliParam.getNumPieceCli());
		}
		if(cliParam.getPieceCli()!=null){
			cli.setPieceCli(cliParam.getPieceCli());
		}
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return "ok";
	}
	
	@GET
	@Path("/deleteclient")
	@Produces("text/plain")
	public String deleteClient(@QueryParam("numCli") int numCli)  throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		Client cli= em.find(Client.class,numCli);
		List<Sejour> sejs=cli.getSejours();
		for(Sejour s : sejs){
			s.removeAllActivites();
			em.remove(s);
		}
		em.remove(cli);
		em.getTransaction().commit();
		HibUtil.closeEntityManager();
		return "ok";
	}
	


	

	
	}
