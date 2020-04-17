
package com.hand.spc.utils.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class RedisHelper_Hzero  {
    private Logger logger = LoggerFactory.getLogger(RedisHelper_Hzero.class);
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOpr;
    @Autowired
    private HashOperations<String, String, String> hashOpr;
    @Autowired
    private ListOperations<String, String> listOpr;
    @Autowired
    private SetOperations<String, String> setOpr;
    @Autowired
    private ZSetOperations<String, String> zSetOpr;
    static ObjectMapper objectMapper = new ObjectMapper();
    public static final long DEFAULT_EXPIRE = 86400L;
    public static final long NOT_EXPIRE = -1L;

    public RedisHelper_Hzero() {
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return this.redisTemplate;
    }

    public void setCurrentDatabase(int database) {
        this.logger.warn("Use default RedisHelper, you'd better use a DynamicRedisHelper instead.");
    }

    public void clearCurrentDatabase() {
        this.logger.warn("Use default RedisHelper, you'd better use a DynamicRedisHelper instead.");
    }

    public void delKey(String key) {
        this.redisTemplate.delete(key);
    }

    public Boolean setExpire(String key) {
        return this.setExpire(key, 86400L, TimeUnit.SECONDS);
    }

    public Boolean setExpire(String key, long expire) {
        return this.setExpire(key, expire, TimeUnit.SECONDS);
    }

    public Boolean setExpire(String key, long expire, TimeUnit timeUnit) {
        return this.redisTemplate.expire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

    public void delKeys(Collection<String> keys) {
        Set<String> hs = new HashSet();
        Iterator var3 = keys.iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            hs.add(key);
        }

        this.redisTemplate.delete(hs);
    }

    private void deleteFullKey(String fullKey) {
        this.redisTemplate.delete(fullKey);
    }

    private void deleteFullKeys(Collection<String> fullKeys) {
        this.redisTemplate.delete(fullKeys);
    }

    public void strSet(String key, String value, long expire, TimeUnit timeUnit) {
        this.valueOpr.set(key, value);
        if (expire != -1L) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }

    }

    public void strSet(String key, String value) {
        this.valueOpr.set(key, value);
    }

    public String strGet(String key) {
        return (String)this.valueOpr.get(key);
    }

    public String strGet(String key, long expire, TimeUnit timeUnit) {
        String value = (String)this.valueOpr.get(key);
        if (expire != -1L) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }

        return value;
    }

    public <T> T strGet(String key, Class<T> clazz) {
        String value = (String)this.valueOpr.get(key);
        return value == null ? null : this.fromJson(value, clazz);
    }

    public <T> T strGet(String key, Class<T> clazz, long expire, TimeUnit timeUnit) {
        String value = (String)this.valueOpr.get(key);
        if (expire != -1L) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }

        return value == null ? null : this.fromJson(value, clazz);
    }

    public String strGet(String key, Long start, Long end) {
        return this.valueOpr.get(key, start, end);
    }

    public Long strIncrement(String key, Long delta) {
        return this.valueOpr.increment(key, delta);
    }

    public Long lstLeftPush(String key, String value) {
        return this.listOpr.leftPush(key, value);
    }

    public Long lstLeftPushAll(String key, Collection<String> values) {
        return this.listOpr.leftPushAll(key, values);
    }

    public Long lstRightPush(String key, String value) {
        return this.listOpr.rightPush(key, value);
    }

    public Long lstRightPushAll(String key, Collection<String> values) {
        return this.listOpr.rightPushAll(key, values);
    }

    public List<String> lstRange(String key, long start, long end) {
        return this.listOpr.range(key, start, end);
    }

    public List<String> lstAll(String key) {
        return this.lstRange(key, 0L, this.lstLen(key));
    }

    public String lstLeftPop(String key) {
        return (String)this.listOpr.leftPop(key);
    }

    public String lstRightPop(String key) {
        return (String)this.listOpr.rightPop(key);
    }

    public Long lstLen(String key) {
        return this.listOpr.size(key);
    }

    public void lstSet(String key, long index, String value) {
        this.listOpr.set(key, index, value);
    }

    public Long lstRemove(String key, long index, String value) {
        return this.listOpr.remove(key, index, value);
    }

    public Object lstIndex(String key, long index) {
        return this.listOpr.index(key, index);
    }

    public void lstTrim(String key, long start, long end) {
        this.listOpr.trim(key, start, end);
    }

    public Long setAdd(String key, String[] values) {
        return this.setOpr.add(key, values);
    }

    public Long setIrt(String key, String... values) {
        return this.setOpr.add(key, values);
    }

    public Set<String> setMembers(String key) {
        return this.setOpr.members(key);
    }

    public Boolean setIsmember(String key, String o) {
        return this.setOpr.isMember(key, o);
    }

    public Long setSize(String key) {
        return this.setOpr.size(key);
    }

    public Set<String> setIntersect(String key, String otherKey) {
        return this.setOpr.intersect(key, otherKey);
    }

    public Set<String> setUnion(String key, String otherKey) {
        return this.setOpr.union(key, otherKey);
    }

    public Set<String> setUnion(String key, Collection<String> otherKeys) {
        return this.setOpr.union(key, otherKeys);
    }

    public Set<String> setDifference(String key, String otherKey) {
        return this.setOpr.difference(key, otherKey);
    }

    public Set<String> setDifference(String key, Collection<String> otherKeys) {
        return this.setOpr.difference(key, otherKeys);
    }

    public Long setDel(String key, String value) {
        return this.setOpr.remove(key, new Object[]{value});
    }

    public Long setRemove(String key, Object[] value) {
        return this.setOpr.remove(key, value);
    }

    public Boolean zSetAdd(String key, String value, double score) {
        return this.zSetOpr.add(key, value, score);
    }

    public Double zSetScore(String key, String value) {
        return this.zSetOpr.score(key, value);
    }

    public Double zSetIncrementScore(String key, String value, double delta) {
        return this.zSetOpr.incrementScore(key, value, delta);
    }

    public Long zSetRank(String key, String value) {
        return this.zSetOpr.rank(key, value);
    }

    public Long zSetReverseRank(String key, String value) {
        return this.zSetOpr.reverseRank(key, value);
    }

    public Long zSetSize(String key) {
        return this.zSetOpr.size(key);
    }

    public Long zSetRemove(String key, String value) {
        return this.zSetOpr.remove(key, new Object[]{value});
    }

    public Set<String> zSetRange(String key, Long start, Long end) {
        return this.zSetOpr.range(key, start, end);
    }

    public Set<String> zSetReverseRange(String key, Long start, Long end) {
        return this.zSetOpr.reverseRange(key, start, end);
    }

    public Set<String> zSetRangeByScore(String key, Double min, Double max) {
        return this.zSetOpr.rangeByScore(key, min, max);
    }

    public Set<String> zSetReverseRangeByScore(String key, Double min, Double max) {
        return this.zSetOpr.reverseRangeByScore(key, min, max);
    }

    public Set<String> zSetRangeByScore(String key, Double min, Double max, Long offset, Long count) {
        return this.zSetOpr.rangeByScore(key, min, max, offset, count);
    }

    public Set<String> zSetReverseRangeByScore(String key, Double min, Double max, Long offset, Long count) {
        return this.zSetOpr.reverseRangeByScore(key, min, max, offset, count);
    }

    public Long zSetCount(String key, Double min, Double max) {
        return this.zSetOpr.count(key, min, max);
    }

    public void hshPut(String key, String hashKey, String value) {
        this.hashOpr.put(key, hashKey, value);
    }

    public void hshPutAll(String key, Map<String, String> map) {
        this.hashOpr.putAll(key, map);
    }

/*    public byte[] hshGetSerial(String key, String hashKey) {
        RedisSerializer<String> redisSerializer = this.redisTemplate.getStringSerializer();
        return (byte[])this.redisTemplate.execute((connection) -> {
            try {
                return connection.hGet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey));
            } catch (Exception var6) {
                this.logger.error("获取HASH对象序列失败", var6);
                return null;
            }
        });
    }

    public Boolean hshPutSerial(String key, String hashKey, byte[] value) {
        RedisSerializer<String> redisSerializer = this.redisTemplate.getStringSerializer();
        return (Boolean)this.redisTemplate.execute((connection) -> {
            try {
                return connection.hSet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey), value);
            } catch (Exception var7) {
                this.logger.error("插入HASH对象序列失败", var7);
                return Boolean.FALSE;
            }
        });
    }*/

    public String hshGet(String key, String hashKey) {
        return (String)this.hashOpr.get(key, hashKey);
    }

    public List<String> hshMultiGet(String key, Collection<String> hashKeys) {
        return this.hashOpr.multiGet(key, hashKeys);
    }

    public Map<String, String> hshGetAll(String key) {
        return this.hashOpr.entries(key);
    }

    public Boolean hshHasKey(String key, String hashKey) {
        return this.hashOpr.hasKey(key, hashKey);
    }

    public Set<String> hshKeys(String key) {
        return this.hashOpr.keys(key);
    }

    public List<String> hshVals(String key) {
        return this.hashOpr.values(key);
    }

    public List<String> hshVals(String key, Collection<String> hashKeys) {
        return this.hashOpr.multiGet(key, hashKeys);
    }

    public Long hshSize(String key) {
        return this.hashOpr.size(key);
    }

    public void hshDelete(String key, Object... hashKeys) {
        this.hashOpr.delete(key, hashKeys);
    }

    public void hshRemove(String key, Object[] hashKeys) {
        this.hashOpr.delete(key, hashKeys);
    }

    public <T> String toJson(T object) {
        if (object == null) {
            return "";
        } else if (!(object instanceof Integer) && !(object instanceof Long) && !(object instanceof Float) && !(object instanceof Double) && !(object instanceof Boolean) && !(object instanceof String)) {
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

    public <T> List<T> fromJsonList(String json, Class<T> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, new Class[]{clazz});

        try {
            return (List)objectMapper.readValue(json, javaType);
        } catch (IOException var5) {
            this.logger.error(var5.getMessage(), var5);
            return new ArrayList();
        }
    }

    public <T> void objectSet(String key, T object) {
        this.strSet(key, this.toJson(object));
    }

    /** @deprecated */
    @Deprecated
    public int deleteKeysWithPrefix(String keyPrefix) {
        Set<String> keys = this.keys(keyPrefix + '*');
        if (keys != null && !keys.isEmpty()) {
            this.deleteFullKeys(keys);
            return keys.size();
        } else {
            return 0;
        }
    }

    /** @deprecated */
    @Deprecated
    public Set<String> keys(String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
