package dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.function.Predicate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.google.inject.Inject;

public class Database<TEntity> implements IDatabase<TEntity> {
	@Inject
	private SessionFactory sessionFactory;
	private Session session;
	
	private Class<TEntity> persistentClass;
	@Inject
	private Logger logger;
	
	@SuppressWarnings("unchecked")
	public Database() {
		this.persistentClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	public Class<TEntity> getPersistentClass() {
		return persistentClass;
	}

	@Override
	public IQueryable<TEntity> all() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass);
		
		return query;
	}

	@Override
	public IDatabase<TEntity> insert(TEntity... entities) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		session.beginTransaction();
		for (TEntity entity : entities) {
			session.save(entity);
		}
		//session.getTransaction().commit();
		return this;
	}

	@Override
	public IDatabase<TEntity> delete(TEntity... entities) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		session.beginTransaction();
		for (TEntity entity : entities) {
			session.delete(entity);
		}
		//session.getTransaction().commit();
		return this;
	}

	@Override
	public IDatabase<TEntity> update(TEntity... entities) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		session.beginTransaction();
		for (TEntity entity : entities) {
			session.update(entity);
		}
		//session.getTransaction().commit();
		return this;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public IQueryable<TEntity> where() {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass);
		//query.a
		//query.list()
		return query;
	}

	@Override
	public TEntity first(Predicate<TEntity> predicate) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria("");
		
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public TEntity first() {
		// TODO Auto-generated method stub
		return null;
	}

}
