/*
 * Copyright (C) 2019 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package photohosting.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class NonceService {
	
	public static final String NONCES_KEY = "nonces".intern();
	
	public Map<String, String> getNonces(Map<String, Object> session) {
		Map<String, String> nonces = (Map<String, String>) session.get(NONCES_KEY);
		
		if (nonces == null) {
			nonces = new HashMap<>();
			
			session.put(NONCES_KEY, nonces);
		}
		
		return nonces;
	}
	
	public String generateNonce(Map<String, Object> session, String id) {
		String uuid = UUID.randomUUID().toString();
		
		getNonces(session).put(id, uuid);
		
		return uuid;
	}
	
	public Map<String, String> generateNonces(Map<String, Object> session, List<String> ids) {
		Map<String, String> map = new HashMap<>();
		
		for (String id : ids) {
			map.put(id, generateNonce(session, id));
		}
		
		return map;
	}
	
}
