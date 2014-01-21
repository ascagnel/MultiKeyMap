package com.ascagnel.MultiKeyMap;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiKeyMapTest {

	private MultiKeyMap<TestType> mkm;

	@Before
	public void setUp() throws Exception {
		this.mkm = new MultiKeyMap<TestType>();
	}

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IntrospectionException {
		TestType item = new TestType();
		item.setValue1("aaaa");
		item.setValue2("bbbb");
		this.mkm.put(item);
		Assert.assertFalse(this.mkm.isEmpty());
		System.out.println(this.mkm.get("value1", "aaaa").getValue2());
	}

	public class TestType {

		private String value1;
		private String value2;

		public String getValue1() {
			return value1;
		}

		public void setValue1(String value1) {
			this.value1 = value1;
		}

		public String getValue2() {
			return value2;
		}

		public void setValue2(String value2) {
			this.value2 = value2;
		}
	}
}
