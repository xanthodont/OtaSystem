package dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.function.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

public class JPADatabase<TEntity> implements IDatabase<TEntity>{
	
	@Inject
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ota-unit");
	private EntityManager em;
	
	private Class<TEntity> persistentClass;
	
	@SuppressWarnings("unchecked")
	public JPADatabase() {
		// TODO Auto-generated constructor stub
		this.persistentClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	public Class<TEntity> getPersistentClass() {
		return persistentClass;
	}
	
	
	@Override
	public IQueryable<TEntity> all() {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<TEntity> criteria = builder.createQuery(persistentClass);
		Root<TEntity> entity = criteria.from(persistentClass); 
		criteria.select(entity);
		//criteria.where(builder.lt(entity.get(), arg1))
		return null;
	}

	@Override
	public IDatabase<TEntity> insert(TEntity... entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatabase<TEntity> delete(TEntity... entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatabase<TEntity> update(TEntity... entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IQueryable<TEntity> where() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEntity first(ICondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(ICondition condition) {
		// TODO Auto-generated method stub
		
		return 0;
	}
	@Override
	public TEntity first() {
		// TODO Auto-generated method stub
		return null;
	}

}
