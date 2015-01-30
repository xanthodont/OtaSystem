package dao.base;

import java.util.List;
import java.util.function.Predicate;

public interface IDatabase<TEntity> {
	IQueryable<TEntity> all();
	
	IDatabase<TEntity> insert(TEntity... entities);
	
	IDatabase<TEntity> delete(TEntity... entities);
	IDatabase<TEntity> delete(ICondition condition);
	
	IDatabase<TEntity> update(TEntity... entities);
	
	void commit(); 
	
	IQueryable<TEntity> where(ICondition condition);
	
	IQueryable<TEntity> first();
	
	long count(ICondition... condition);
	IQueryable<TEntity> count();

	TEntity first(ICondition condition);
}
