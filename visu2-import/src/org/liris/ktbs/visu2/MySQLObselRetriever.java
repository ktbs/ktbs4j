package org.liris.ktbs.visu2;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

public class MySQLObselRetriever {

	public Collection<String> getRdfFields(String like) {
		Configuration cfg = new Configuration().addResource("hibernate.cfg.xml");

		SessionFactory sessionFactory = cfg.configure().buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		List<?> result = session.createQuery( "from Obsel where rdf like '"+like+"'" ).list();

		List<String> rdfFields = new LinkedList<String>();
		
		for(Object r : (List<?>) result) {
			Obsel obsel = (Obsel) r;
			rdfFields.add(obsel.getRdfText());
		}
		
		session.getTransaction().commit();
		session.close();
		
		return rdfFields;	
	}

	public Collection<String> getRdfFields() {
		Configuration cfg = new Configuration().addResource("hibernate.cfg.xml");
		
		SessionFactory sessionFactory = cfg.configure().buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		List<?> result = session.createQuery( "from Obsel").list();
		
		List<String> rdfFields = new LinkedList<String>();
		
		for(Object r : (List<?>) result) {
			Obsel obsel = (Obsel) r;
			rdfFields.add(obsel.getRdfText());
		}
		
		session.getTransaction().commit();
		session.close();
		
		return rdfFields;	
	}

	public Collection<String> getObselTypes() {
		Configuration cfg = new Configuration().addResource("hibernate.cfg.xml");
		
		SessionFactory sessionFactory = cfg.configure().buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		List<?> result = session.createQuery( "from Obsel" ).list();
		
		Set<String> types = new HashSet<String>();
		
		for(Object r : (List<?>) result) {
			Obsel obsel = (Obsel) r;
			types.add(obsel.getType());
		}
		
		session.getTransaction().commit();
		session.close();
		
		return types;	
	}

}
