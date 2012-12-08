/**
 * 
 */
package com.tk.bzintegration.properties;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;


public class BZIntegrationProperties {

	private static final String DEFAULT_BZ_RPC_ADDRESS = "https://shawnee.pentesters.pl/bugzilla/xmlrpc.cgi";
	private static final String DEFAULT_AUTHENTICATION_ON="true";

	/**
	 * Instance of the class. Singleton.
	 */
	private static BZIntegrationProperties instance;
	/**
	 * Properties map
	 */
	private static HashMap<PropertyKey, String> map = new HashMap<PropertyKey, String>();

	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * Private constructor - singleton.
	 */
	private BZIntegrationProperties() {
		map.put(PropertyKey.BZ_RPC_ADDRESS, DEFAULT_BZ_RPC_ADDRESS);
		map.put(PropertyKey.AUTHENTICATION_ON, DEFAULT_AUTHENTICATION_ON);
	}

	/**
	 * Returns instance of this class.
	 * 
	 * @return instance of PatchmasterProperties
	 */
	public static BZIntegrationProperties getInstance() {
		if (instance == null) {
			instance = new BZIntegrationProperties();
		}
		return instance;
	}

	/**
	 * Sets properties loaded from file.
	 * 
	 * @param properties
	 *            properties to be set in map
	 */
	public void setProperties(Properties properties) {
		Set<Object> keys = properties.keySet();
		PropertyKey[] propertyKeys = PropertyKey.values();
		for (int i = 0; i < propertyKeys.length; i++) {
			String propertyKey = propertyKeys[i].toString();
			boolean found = false;
			for (Object object : keys) {
				String key = (String) object;
				if (key.equals(propertyKey)) {
					map.put(propertyKeys[i], properties.getProperty(key));
					found = true;
					break;
				}
			}
			if (!found) {
				logger.warn("Property:" + propertyKey
						+ " not found in properties file.");
			}
		}
		logger.info("Properties updated.");
	}

	/**
	 * Returns property value for given key
	 * 
	 * @param key
	 *            {@link PropertyKey} enumeration key value
	 * @return String property value
	 */
	public String getProperty(PropertyKey key) {
		return map.get(key);
	}
	
	/**
	 * Returns property value for given key with main folder prefix
	 * 
	 * @param key
	 *            {@link PropertyKey} enumeration key value
	 * @return String property value
	 */
	public String getPropertyWithMainFolderPrefix(PropertyKey key) {
		String path = getProperty(PropertyKey.MAIN_FOLDER);
		return path + File.separator + getProperty(key);
	}
}
