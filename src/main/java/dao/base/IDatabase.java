package dao.base;

import java.util.List;
import java.util.function.Predicate;

public interface IDatabase<TEntity> {

	
	IDatabase<TEntity> insert(TEntity... entities);
	
	IDatabase<TEntity> delete(TEntity... entities);
	IDatabase<TEntity> delete(ICondition condition);
	
	IDatabase<TEntity> update(TEntity... entities);
	
	void commit(); 

	IQueryable<TEntity> all();
	IQueryable<TEntity> first();
	IQueryable<TEntity> count();
	
	long count(ICondition... condition);
	TEntity first(ICondition... condition);
}
