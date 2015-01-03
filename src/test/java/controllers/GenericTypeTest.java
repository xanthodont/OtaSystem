package controllers;

import java.lang.reflect.ParameterizedType;

import org.junit.Test;


public class GenericTypeTest {
	@Test
	public void parentTest() {
		Parent<String> p = new Child<String>();
	}
}


class Parent<T> {

	public Parent() {
		ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
		System.out.println("type==" + type);
		System.out.println("entityClass==" + type.getActualTypeArguments()[0]);
		System.out.println("getOwnerType==" + type.getOwnerType());
		System.out.println("getRawType==" + type.getRawType());
	}
}

class Child<T> extends Parent<String> {
}
