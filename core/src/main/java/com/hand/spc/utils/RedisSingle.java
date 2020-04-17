
package com.hand.spc.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisSingle {
	private Logger logger = LoggerFactory.getLogger(RedisSingle.class);
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	static ObjectMapper objectMapper = new ObjectMapper();

	private HashMap<String, String> hashMap=new HashMap<String,String>();

	private static RedisSingle instance = new RedisSingle();

	private RedisSingle() {
	}

	public static RedisSingle getInstance() {
		return instance;
	}

	public void strSet(String key, String value) {
		this.hashMap.put(key, value);
	}

	public String strGet(String key) {
		return this.hashMap.get(key);
	}

	public <T> String toJson(T object) {
		if (object == null) {
			return "";
		} else if (!(object instanceof Integer) && !(object instanceof Long) && !(object instanceof Float)
				&& !(object instanceof Double) && !(object instanceof Boolean) && !(object instanceof String)) {
			try {
				return objectMapper.writeValueAsString(object);
			} catch (JsonProcessingException var3) {
				return "";
			}
		} else {
			return String.valueOf(object);
		}
	}

	public <T> T fromJson(String json, Class<T> clazz) {
		if (!StringUtils.isBlank(json) && clazz != null) {
			try {
				return objectMapper.readValue(json, clazz);
			} catch (Exception var4) {
				if (this.logger.isErrorEnabled()) {
					this.logger.error(var4.getMessage(), var4);
				}

				return null;
			}
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> fromJsonList(String json, Class<T> clazz) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, new Class[] { clazz });

		try {
			return (List) objectMapper.readValue(json, javaType);
		} catch (IOException var5) {
			this.logger.error(var5.getMessage(), var5);
			return new ArrayList();
		}
	}
	
	public HashMap<String, String> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, String> hashMap) {
		this.hashMap = hashMap;
	}

}
