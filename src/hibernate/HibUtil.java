package hibernate;
				
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibUtil {
	public static  EntityManager entityManager;
	public static EntityManagerFactory entityManagerFactory;

	static {
		try {		
			entityManagerFactory = Persistence.createEntityManagerFactory("ProjetRestTomcat");
			entityManager = entityManagerFactory.createEntityManager();
			// Création de la SessionFactory à partir de hibernate.cfg.xml
		//	sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you lo the exception, as it might be swallowed
			System.err.println("Initial EntityManager init error." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static final ThreadLocal session = new ThreadLocal();

	public static EntityManager getEntityManager(){
		if(!entityManager.isOpen()){
			entityManagerFactory = Persistence.createEntityManagerFactory("ProjetRestTomcat");
			entityManager = entityManagerFactory.createEntityManager();
		}
		return entityManager;
	}
	
	public static void closeEntityManager(){
		entityManager.close();
		 entityManagerFactory.close();
	}
}