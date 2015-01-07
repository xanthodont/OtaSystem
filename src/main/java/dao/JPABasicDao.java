package dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.sf.ehcache.search.expression.Criteria;

import org.hibernate.Session;

public class JPABasicDao<TEntity> implements IBasicDao<TEntity>{
	
	private Class<TEntity> entityClass;

	//@PersistenceContext(unitName="ota-unit")
    private EntityManager em;
	
	public JPABasicDao() {
		EntityManagerFactory facroty = Persistence.createEntityManagerFactory("ota-unit");
		em = facroty.createEntityManager();
		
		//Type genType = this.getClass().getGenericInterfaces()[0];
        //Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
        //entityClass = (Class<TEntity>) params[0];
	}
	
	@SuppressWarnings("unchecked")
	public List<TEntity> all() {
		// TODO Auto-generated method stub
		CriteriaQuery<TEntity> criteria = (CriteriaQuery<TEntity>) em.getCriteriaBuilder().createQuery(entityClass);
		Root<TEntity> root = (Root<TEntity>) criteria.from(entityClass);
		criteria.select(root);
		return em.createQuery(criteria).getResultList();
	}
	
	//public List<TEntity> get() {}

	public IBasicDao<TEntity> insert(TEntity... entities) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		for (TEntity entity : entities) {
			em.persist(entity);
		}
		return this;
	}

	public IBasicDao<TEntity> delete(TEntity... entities) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		for (TEntity entity : entities) {
			em.remove(entity);
		}
		return this;
	}

	public IBasicDao<TEntity> update(TEntity entity) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.merge(entity);
		
		return this;
	}
	
	public IBasicDao<TEntity> refresh(TEntity entity) {
		em.refresh(entity);
		return this;
	}

	public void commit() {
		// TODO Auto-generated method stub
		em.flush();
		em.getTransaction().commit();
	}

}
