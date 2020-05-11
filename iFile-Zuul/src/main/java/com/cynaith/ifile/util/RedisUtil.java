package com.cynaith.ifile.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: Cynaith
 **/

public class RedisUtil {
    static JedisPool jedisPool = null;
    static {
        GenericObjectPoolConfig poolConfig = new JedisPoolConfig();
        //最大连接数
        poolConfig.setMaxTotal(30);
        //最大空闲数
        poolConfig.setMaxIdle(10);
        //超时时间
        poolConfig.setMaxWaitMillis(3*1000);
        jedisPool = new JedisPool(poolConfig,"106.12.130.1",9001,5*1000,"admin");
    }

    public static void set(String key,String value){
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null)
                jedis.close();
        }
        System.out.println("Redis set :"+key+"+"+value);
    }

    public static String get(String key){
        Jedis jedis = jedisPool.getResource();
        String res = "";
        try {
            res = jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null)
                jedis.close();
        }
        return res;
    }
}
