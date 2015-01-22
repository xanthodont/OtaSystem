package dao.base;

import org.junit.Before;
import org.junit.Test;

import dao.base.Queryable.OperateType;

public class QueryableTest {
	private Queryable<String> query; 
	
	@Before
	public void init() {
		query = new Queryable<String>(null, String.class, OperateType.count);
	}
	
	@Test
	public void countTest(){
		//query = new Queryable<String>(null, String.class, OperateType.count);
		//query.toCount();
	}
	
	@Test
	public void whereTest() {
		query.where(c -> c.equals("name", "1231"))
			 .and(c -> c.notEquals("email", "kon@konka.com"))
			 .or(c -> c.equals("nickname", "nick"))
			 .orderBy(true, "name", "email")
			 .limit(1, 10);
		String sql = query.toSqlString();
		System.out.println(sql);
	}
}
