package dao;

/**
 * sql属�?
 * @author jan
 *
 */
public class SqlProperty 
{
	public final String EQUAL = "="; 
	public final String NOT_EQUAL = "!=";
	
	private String name;
	private Object value;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void equal(String name, Object value) {
		this.setName(name);
		this.setValue(value);
		this.setType(EQUAL);
	}
	
	public void notEqual(String name, Object value) {
		this.setName(name);
		this.setValue(value);
		this.setType(NOT_EQUAL);
	}
}
