package ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import cerisaie.WSClient;
import cerisaie.WSSejour;

// addresse de basse pour accéder au WS
@ApplicationPath("/")
public class RestfullApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	public RestfullApplication() {
		singletons.add(new WSRessource());
		singletons.add(new WSClient());
		singletons.add(new WSSejour());
	}
		
	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
