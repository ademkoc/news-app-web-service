package com.ademkoc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private SessionFactory factory = null;


	private static HibernateUtil hibernateUtil = null;

	public static HibernateUtil getInstance() {
		if (hibernateUtil == null) {
			hibernateUtil = new HibernateUtil();
		}
		return hibernateUtil;
	}

	private HibernateUtil() {

	}

	public SessionFactory getFactory() {
		try {
			if (factory == null) {
				factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			} else if (factory.isClosed()) {
				factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			}
		} catch (Exception e) {
			System.err.println("Exception in getFactory while creating new Factory M: "
					+ e.getMessage() + " C: " + e.getCause() + " ST: "
					+ e.getStackTrace());
		}
		return factory;
	}

	public Session getCurrentSession() {
		Session session = null;
		try {
			session = getFactory().getCurrentSession();
		} catch (Exception e) {
			System.err.println("Exception in getCurrentSession M: " + e.getMessage()
					+ " C: " + e.getCause());
		}
		return session;
	}
}