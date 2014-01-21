package com.ascagnel.MultiKeyMap;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Create a map addressable by multiple keys.
 * 
 * @author ascagnel
 * 
 * @param <K>
 * @param <T>
 */
public class MultiKeyMap<T> implements Serializable {
	private static final long serialVersionUID = 4398841554734749331L;

	/**
	 * The indices of all items in the actual map. First parameter in outer map
	 * is the key name, first parameter of inner map is the corresponding data
	 * values for the key, and second parameter of inner map is the index of the
	 * data in the list.
	 */
	private Map<String, HashMap<Object, Integer>> index = new HashMap<String, HashMap<Object, Integer>>();
	private List<T> data = new ArrayList<T>();

	public MultiKeyMap() {

	}

	/**
	 * Using reflection, reads all formal getters (methods with no arguments and
	 * begin with "get" or "is" and adds them as indices to the MultiKeyMap.
	 * 
	 * @param input
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException 
	 */
	public void put(T input) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IntrospectionException {
		this.data.add(input);
		Integer inputIndex = this.data.indexOf(input);

		for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(input.getClass(), Object.class).getPropertyDescriptors()) {
//		for (Method method : input.getClass().getDeclaredMethods()) {
			
			String propertyName = propertyDescriptor.getName();
			Method method = propertyDescriptor.getReadMethod();
			
			if (method.getParameterTypes().length != 0
					&& !propertyName.startsWith("get")
					&& !propertyName.startsWith("is")) {
				continue;
			}

			if (!this.index.containsKey(propertyName) || this.index.get(propertyName) == null) {
				this.index.put(propertyName, new HashMap<Object, Integer>());
			}
			
			System.out.println(propertyName);
			this.index.get(propertyName).put(method.invoke(input, new Object[0]), inputIndex);
		}
	}

	/**
	 * Given a key and value lookup pair, will return a data item or null if the
	 * item does not exist.
	 * 
	 * @param keyType
	 * @param keyValue
	 * @return
	 */
	public T get(String keyType, Object keyValue) {
		if (!this.index.containsKey(keyType)) {
			return null;
		}

		if (!this.index.get(keyType).containsKey(keyValue)) {
			return null;
		}
		return this.data.get(this.index.get(keyType).get(keyValue));
	}

	public Iterator<T> iterator() {
		return this.data.iterator();
	}

	public boolean containsKeyType(Class<?> key) {
		return this.index.containsKey(key.getName());
	}

	public boolean isEmpty() {
		return this.data.isEmpty();
	}
}
