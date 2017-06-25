package com.ademkoc.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.ademkoc.util.HibernateUtil;

@SuppressWarnings("unchecked")
public abstract class BaseHibernateDAO<T extends Serializable>{
	
	private HibernateUtil hibernateUtil;
    private Class<T> type;

	public BaseHibernateDAO() {
		hibernateUtil = HibernateUtil.getInstance();
		type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected Session getCurrentSession() {
		return hibernateUtil.getCurrentSession();
	}
	
	public Class<T> getPersistentClass() {
		return type;
	}

	public Serializable save(T entity) {
		Serializable serializable = null;

		if (entity == null) {
			throw new IllegalArgumentException("Entity must not be null");
		}

		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			serializable = session.save(entity);
			transaction.commit();
		} catch (HibernateException e) {
			System.err.printf("\nError while saving Entity. M: " + e.getMessage()
					+ " C: " + e.getCause(), e);
		}
		return serializable;
	}
	
	public void saveOrUpdate(T entity){
		if (entity == null) {
			throw new IllegalArgumentException("Entity must not be null");
		}

		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(entity);
			transaction.commit();
		} catch (HibernateException e) {
			System.err.printf("\nError while saveOrUpdate Entity. M: " + e.getMessage()
					+ " C: " + e.getCause(), e+"\n");
			throw new HibernateException(e);
		}
	}
	
	public void delete(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity Must not be Null");
		}

		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			session.delete(entity);
			transaction.commit();
		} catch (HibernateException e) {
			System.err.printf("Error while delete Entity. M: " + e.getMessage()
					+ " C: " + e.getCause(), e);
		}
	}
	
	public List<T> getAll() {
		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			List<T> list = session.createQuery("from "+getPersistentClass().getSimpleName()).list();
			transaction.commit();
			return list;
		} catch (Exception e) {
			System.err.println("\nError while getAll Entities. M: " + e.getMessage()
					+ " C: " + e.getCause());
		}
		return null;
	}
	
	public List<T> getAllByHQL(String query){
		List<T> ts = null;
		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			ts = session.createQuery(query).list();
			transaction.commit();
			return ts;
		} catch (Exception e) {
			System.err.println("\nError while getAllByHQL Entities. M: " + e.getMessage()
					+ " C: " + e.getCause());
		}
		return null;
	}
	
	public List<T> getAllByHQL(String query, Map<String, List<String>> params) {
		List<T> ts = null;
		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			Query<T> q = session.createQuery(query);
				
			for (int i = 0; i < params.size(); i++) {
				String key = params.keySet().toArray()[i].toString();
				
				List<String> list = params.get(key);
				for (int j = 0; j < list.size(); j++) {
					q.setParameter(key, list.get(j));
				}
			}
			
			ts = q.list();

			transaction.commit();
		} catch (Exception e) {
			System.err.printf("\nError while getAllByHQL Entities. M: " + e.getMessage()
					+ " C: " + e.getCause() + " SQL: " + query, e);
		}
		return ts;
	}
	
	public T getById(Integer id) {
		T entity = null;
		try {
			Session session = this.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			entity = (T) session.get(type, id);
			transaction.commit();
		} catch (Exception e) {
			System.err.printf("\nError while getById Entity. M: " + e.getMessage()
					+ " C: " + e.getCause(), e);
		}
		return entity;
	}
}
