/*
 * Copyright (C) 2019 Nathan Crause
 *
 * This file is part of project Vulcan.
 */
package photohosting.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class providing some easy means of quickly creating maps
 *
 * @author n.crause
 */
public class Maps {
	
	/**
	 * This class simply acts as a proxy to an underlying Map instance to
	 * provide a <code>put</code> method capable of chaining.
	 * 
	 * @param <M> 
	 */
	public static class Builder<M extends Map> {
		
		private final M map;

		public Builder(Class<M> type) throws InstantiationException, IllegalAccessException {
			map = type.newInstance();
		}
		
		/**
		 * Returns the underlying <code>Map</code> instance.
		 * 
		 * @return 
		 */
		public M getMap() {
			return map;
		}
		
		public Builder<M> put(Object key, Object value) {
			map.put(key, value);
			
			return this;
		}
		
	}
	
	public static <M extends Map> Builder start(Class<M> type) {
		try {
			return new Builder(type);
		} 
		catch (InstantiationException | IllegalAccessException ex) {
			throw new RuntimeException("Unexpected failed to instantiate map", ex);
		}
	}
	
	public static Builder<HashMap> start() {
		return start(HashMap.class);
	}
	
}
