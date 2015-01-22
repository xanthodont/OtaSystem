package dao.base;

import java.util.List;
import java.util.function.Predicate;

public interface IDatabase<TEntity> {
	IQueryable<TEntity> all();
	
	IDatabase<TEntity> insert(TEntity... entities);
	
	IDatabase<TEntity> delete(TEntity... entities);
	
	IDatabase<TEntity> update(TEntity... entities);
	
	void commit(); 
	
	IQueryable<TEntity> where();
	
	TEntity first();
	
	long count(ICondition condition);

	TEntity first(ICondition condition);
}
