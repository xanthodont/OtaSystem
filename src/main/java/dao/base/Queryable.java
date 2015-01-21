package dao.base;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;

import com.google.inject.Inject;

import dao.SqlProperty;
import dao.extensions.IPageList;

public class Queryable<TEntity> extends ArrayList implements IQueryable<TEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Session session;
	private StringBuilder sql;
	private Class<TEntity> persistentClass;
	
	private List<SqlProperty> andCondition;
	private List<SqlProperty> orCondition;
	private Map<String, SqlProperty> whereCondition;
	private LimitCondition limitCondition;
	private OrderByCondition orderByCondition; 
	
	@Inject
	private Logger logger;

	private List<Object> paramValueList;

	public Queryable(Session session, Class<TEntity> persistentClass) {
		// TODO Auto-generated constructor stub
		this.session = session;
		this.persistentClass = persistentClass;
		this.sql = new StringBuilder();
		this.whereCondition = new HashMap<String, SqlProperty>();
		this.andCondition = new ArrayList<SqlProperty>();
		this.orCondition = new ArrayList<SqlProperty>();
		this.paramValueList = new ArrayList<Object>();
		
	}
	
	public Queryable append(String s) {
		this.sql.append(s);
		return this;
	}
	
	public Queryable where(ICondition and) {
		//sql.append(" WHERE ").append("");//predicate.
		SqlProperty sp = new SqlProperty();
		and.condition(sp);
		andCondition.add(sp);
		return this;
	}
	
	public Queryable and(ICondition and) {
		//sql.append(" WHERE ").append("");//predicate.
		SqlProperty sp = new SqlProperty();
		and.condition(sp);
		andCondition.add(sp);
		return this;
	}
	public Queryable or(ICondition or) {
		//sql.append(" WHERE ").append("");//predicate.
		SqlProperty sp = new SqlProperty();
		or.condition(sp);
		orCondition.add(sp);
		return this;
	}
	
	public Queryable limit(int page, int size) {
		limitCondition = new LimitCondition(page, size);
		return this;
	}
	
	public Queryable orderBy(boolean order, String... properties) {
		this.orderByCondition = new OrderByCondition(order, properties);
		return this;
	}

	@Override
	public List<TEntity> toList() {
		// TODO Auto-generated method stub
		Query query = session.createQuery(toSqlString());
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}
		
		@SuppressWarnings("unchecked")
		List<TEntity> list = session.createQuery(sql.toString()).list();
		session.close();
		return list;
	}

	@Override
	public IPageList<TEntity> toPageList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toSqlString() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		paramValueList = new ArrayList<Object>();
		builder.append(" WHERE ");
		for (int i = 0, size = andCondition.size(); i < size; i++) {
			SqlProperty sqlProperty = andCondition.get(i);
			if (i != 0) {
				builder.append(" AND ");
			}
			builder.append(sqlProperty.getName())
				   .append(sqlProperty.getType())
				   .append("?");
			paramValueList.add(sqlProperty.getValue());
		}
		for (int i = 0, size = orCondition.size(); i < size; i++) {
			SqlProperty sqlProperty = orCondition.get(i);
			if (paramValueList.size() > 0 || i != 0) {
				builder.append(" OR ");
			}
			builder.append(sqlProperty.getName())
			   	   .append(sqlProperty.getType())
			       .append("?");
			paramValueList.add(sqlProperty.getValue());
		}
		if (orderByCondition != null) {
			builder.append(" ORDER BY ")
				   .append(orderByCondition.getProperties())
				   .append(orderByCondition.getOrder() ? " ASC " : " DESC ");
		}
		if (limitCondition != null) {
			builder.append(" LIMIT ")
				   .append((limitCondition.getPage()-1)*limitCondition.getSize())
				   .append(",")
				   .append(limitCondition.getSize());
		}
		
		return builder.toString();
	}

}
