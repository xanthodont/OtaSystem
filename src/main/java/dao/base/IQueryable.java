package dao.base;

import java.util.List;

import dao.extensions.IPageList;

public interface IQueryable<TEntity> extends List {
	IQueryable where(ICondition w);
	
	String toSqlString();
	
	List<TEntity> toList();
	
	IPageList<TEntity> toPageList(); 
}
