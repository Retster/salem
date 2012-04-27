package org.apxeolog.salem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *  == Haven and Hearth Extended ==
 * Class for save/load settings. This class use reflection.
 * Just add public static field and it will be saved into haven.cfg
 * Hardcode default values, this mechanism do not provide them.
 * @author APXEOLOG
 *
 */
public class HConfig {
	public static boolean cl_no_minimap_chache_limit = false;
	
	private static Properties configFile = new Properties();
	private static HashMap<String, String> additionTokens= new HashMap<String, String>();
	
	public static void addValue(String name, Object value) {
		additionTokens.put(name, value.toString());
	}
	
	public static <T, V> T getValue(String name, Class vclass) {
		Object ret = additionTokens.get(name);
		return castToType(vclass, ret);
	}
	
	@SuppressWarnings("unchecked")
	private static <T, V> T castToType(V classType, Object value) {
		if (classType.equals(Integer.class)) {
			return (T) Integer.valueOf((String)value);
		} else if (classType.equals(Long.class)) {
			return (T) Long.valueOf((String)value);
		} else if (classType.equals(String.class)) {
			return (T) (String)value;
		} else if (classType.equals(Boolean.class)) {
			return (T) Boolean.valueOf((String)value);
		} else if (classType.equals(Float.class)) {
			return (T) Float.valueOf((String)value);
		} else return null;
	}
	
	public static void loadConfig() {
		try {
			configFile.load(new FileInputStream(new File("salem.cfg")));
			for (Entry<Object, Object> entry : configFile.entrySet()) {
				String filedName = (String) entry.getKey();
					try {
						Field optionField = HConfig.class.getField(filedName);
						if (Modifier.isStatic(optionField.getModifiers()) && Modifier.isPublic(optionField.getModifiers())) {
							optionField.set(null, castToType(optionField.getType(), entry.getValue()));
						}
					} catch (NoSuchFieldException e) {
						addValue(filedName, entry.getValue());
					} catch (SecurityException e) {
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
			}
		} catch (IOException e) {
		} finally {
			configFile.clear();
		}
	}
	
	public static void saveConfig() {
		try {
			Field[] configFields = HConfig.class.getFields();
			for (Field field : configFields) {
				if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
					try {
						configFile.put(field.getName(),	String.valueOf(field.get(null)));
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
				}
			}
			File config = new File("salem.cfg");
			if (config.exists()) config.createNewFile();
			
			for (Map.Entry<String, String> set : additionTokens.entrySet()) {
				configFile.put(set.getKey(), set.getValue());
			}
			configFile.store(new FileOutputStream(config), "Salem Config File");
		} catch (IOException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			configFile.clear();
		}
	}
}