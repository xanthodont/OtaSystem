package dao.base;

import org.junit.Before;
import org.junit.Test;

public class QueryableTest {
	private Queryable<String> query; 
	
	@Before
	public void init() {
		query = new Queryable<String>(null, String.class);
	}
	
	@Test
	public void whereTest() {
		query.where(c -> c.equal("name", "1231"))
			 .and(c -> c.notEqual("email", "kon@konka.com"))
			 .or(c -> c.equal("nickname", "nick"))
			 .orderBy(true, "name", "email")
			 .limit(1, 10);
		String sql = query.toSqlString();
		System.out.println(sql);
	}
}
