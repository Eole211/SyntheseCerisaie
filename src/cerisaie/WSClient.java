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

	
	@POST
	@Path("/getclientsOld")
	@Produces("text/plain")
	// http://localhost:8080/ProjetRestFull/eources/getjson
	public String  getEtudiantToJSON() throws ParseException {
		EntityManager em=HibUtil.getEntityManager();
		em.getTransaction().begin();	
		List clis =  em.createQuery("from Client").getResultList();
		
		try {	
				JSONObject root=new JSONObject();
				JSONArray ja= new JSONArray();
			for(Object o :clis){
				Client c= (Client)o;
					JSONObject jo = new JSONObject();
					jo.put("numCli", c.getNumCli());		
					jo.put("nomCli", c.getNomCli());
					jo.put("adrRueCli", c.getAdrRueCli());
					ja.put(jo);					
			}
			root.put("clients",ja);
			return root.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally{
			em.getTransaction().commit();	
			HibUtil.closeEntityManager();
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
	@Path("updateClient")
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
