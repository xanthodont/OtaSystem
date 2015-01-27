package dao.base;

import java.util.List;

import dao.extensions.IPageList;

public interface IQueryable<TEntity> extends List {
	IQueryable where(ICondition w);
	
	String toSqlString();
	
	long toCount();
	int exec();
	
	IQueryable and(ICondition and);
	IQueryable or(ICondition or);
	
	List<TEntity> toList();
	
	IPageList<TEntity> toPageList(); 
}
