package dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javax.inject.Singleton;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import com.google.inject.Inject;

import dao.base.Queryable.OperateType;

public class Database<TEntity> implements IDatabase<TEntity> {
	@Singleton
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
		//sessionFactory.
		session = sessionFactory.openSession(); 
		session.beginTransaction();
		session.clear();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass, OperateType.all);
		
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
	public IDatabase<TEntity> delete(ICondition condition) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass, OperateType.delete);
		query.exec();
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
		//session.flush();
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public TEntity first(ICondition... conditions) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		session.beginTransaction();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass, OperateType.first);
		for (ICondition condition : conditions) {
			query.and(condition);
		}
		List<TEntity> list = query.toList();
		if (list.size() == 0) return null;
		return (TEntity) list.get(0); 
	}

	@Override
	public long count(ICondition... conditions) {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass, OperateType.count);
		for (ICondition condition : conditions) {
			query.and(condition);
		}
		return query.toCount();
	}
	@Override
	public IQueryable<TEntity> count() {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		session.beginTransaction();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass, OperateType.count);
		
		return query;
	}
	
	@Override
	public IQueryable<TEntity> first() {
		// TODO Auto-generated method stub
		session = sessionFactory.openSession();
		session.beginTransaction();
		IQueryable<TEntity> query = new Queryable<TEntity>(session, persistentClass, OperateType.first);
		return query;
	}
	
	

}
