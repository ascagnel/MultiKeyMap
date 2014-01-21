package com.ascagnel.MultiKeyMap;

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
	 */
	public void put(T input) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		this.data.add(input);
		Integer inputIndex = this.data.indexOf(input);

		for (Method method : input.getClass().getMethods()) {
			if (method.isAccessible()
					&& method.getParameterTypes().length == 0
					&& (method.getName().startsWith("get") || method.getName()
							.startsWith("is"))
					&& this.index.containsKey(method.getName())
					&& this.index.get(method.getName()) != null) {
				this.index.get(method.getName()).put(
						method.invoke(input, new Object[0]), inputIndex);
			}
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
	public T get(Class<?> keyType, Object keyValue) {
		if (!this.index.containsKey(keyType.getName())) {
			return null; 
		}
		
		if (!this.index.get(keyType.getName()).containsKey(keyValue)) {
			return null;
		}
		return this.data.get(this.index.get(keyType.getName()).get(keyValue));
	}

	public Iterator<T> iterator() {
		return this.data.iterator();
	}
	
	public boolean containsKeyType(Class<?> key) {
		return this.index.containsKey(key.getName());
	}
}
