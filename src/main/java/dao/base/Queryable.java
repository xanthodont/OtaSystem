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
	private Class<TEntity> persistentClass;
	
	private List<SqlProperty> andCondition;
	private List<SqlProperty> orCondition;
	private Map<String, SqlProperty> whereCondition;
	private LimitCondition limitCondition;
	private OrderByCondition orderByCondition; 
	
	public enum OperateType { all, select, delete, update, first, count; }
	private OperateType opType;
	
	@Inject
	private Logger logger;

	private List<Object> paramValueList;

	public Queryable(Session session, Class<TEntity> persistentClass, OperateType opType) {
		// TODO Auto-generated constructor stub
		this.session = session;
		this.persistentClass = persistentClass;
		this.whereCondition = new HashMap<String, SqlProperty>();
		this.andCondition = new ArrayList<SqlProperty>();
		this.orCondition = new ArrayList<SqlProperty>();
		this.paramValueList = new ArrayList<Object>();
		
		this.opType = opType;
	}
	
	public Queryable append(String s) {
		return this;
	}
	
	public Queryable where(ICondition and) {
		//sql.append(" WHERE ").append("");//predicate.
		if (and != null) {
			SqlProperty sp = new SqlProperty();
			and.condition(sp);
			andCondition.add(sp);
		}
		return this;
	}
	
	public Queryable and(ICondition and) {
		//sql.append(" WHERE ").append("");//predicate.
		if (and != null) {
			SqlProperty sp = new SqlProperty();
			and.condition(sp);
			andCondition.add(sp);
		}
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
	
	private Query getQuery() {
		System.out.println(toSqlString());
		Query query = session.createQuery(toSqlString());
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}
		return query;
	}
	
	public int exec() {
		int r = getQuery().executeUpdate();
		session.close();
		return r;
	}
	
	public long toCount() {
		List<TEntity> list = getQuery().list();
		long count = (long) list.get(0);
		session.close();
		return count;
	}

	@Override
	public List<TEntity> toList() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<TEntity> list = getQuery().list();
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
		switch (opType) {
		case all:
			builder.append("FROM ").append(persistentClass.getName());
			break;
		case select:
			builder.append("FROM ").append(persistentClass.getName());
			break;	
		case delete:
			builder.append("DELETE FROM ").append(persistentClass.getName());
			break;
		case update:
			break;
		case first:
			builder.append("FROM ").append(persistentClass.getName());
			break;
		case count:
			builder.append("SELECT COUNT(*) FROM ").append(persistentClass.getName());
			break;
		}
		paramValueList = new ArrayList<Object>();
		if (andCondition.size() > 0 || orCondition.size() > 0) {
			builder.append(" WHERE ");
			for (int i = 0, size = andCondition.size(); i < size; i++) {
				SqlProperty sqlProperty = andCondition.get(i);
				if (sqlProperty != null) {
					if (i != 0) {
						builder.append(" AND ");
					}
					builder.append(sqlProperty.getName())
						   .append(sqlProperty.getType())
						   .append("?");
					paramValueList.add(sqlProperty.getValue());
				}
			}
			for (int i = 0, size = orCondition.size(); i < size; i++) {
				SqlProperty sqlProperty = orCondition.get(i);
				if (sqlProperty != null) {
					if (paramValueList.size() > 0 || i != 0) {
						builder.append(" OR ");
					}
					builder.append(sqlProperty.getName())
					   	   .append(sqlProperty.getType())
					       .append("?");
					paramValueList.add(sqlProperty.getValue());
				}
			}
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

	@Override
	public TEntity toEntity() {
		// TODO Auto-generated method stub
		List<TEntity> list = getQuery().list();
		session.close();
		if (list.size() < 1) return null;
		return list.get(0);
	}

}
