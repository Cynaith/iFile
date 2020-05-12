package com.cynaith.ifile.util;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: Cynaith
 **/
public class SessionUtil {

    /**
     * 操作数据库 Redis-Map <iFile-SessionID,<ip,SessionID>>
     * 检查ip是否存在
     *
     * @param ip
     * @return
     */
    public static boolean haveIpAddress(String ip) {
        Jedis jedis = RedisUtil.jedisPool.getResource();
        boolean have = false;
        try {
            have = jedis.hexists("iFile-SessionID", ip);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return have;
    }


    /**
     * 操作数据库 Redis-Map <iFile-SessionID,<ip,SessionID>>
     * 添加 ip，SessionId
     *
     * @param key
     * @param value
     */
    public static void saveSessionId(String key, String value) {
        Jedis jedis = RedisUtil.jedisPool.getResource();
        try {
            jedis.hset("iFile-SessionID", key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    /**
     * 操作数据库 Redis-Map <iFile-SessionID,<ip,SessionID>>
     * 根据 ip 获取 SessionID
     *
     * @param key
     * @return
     */
    public static String getSessionId(String key) {
        Jedis jedis = RedisUtil.jedisPool.getResource();
        String value = "";
        try {
            value = jedis.hmget("iFile-SessionID", key).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * 操作数据库 Redis-Map <iFile-SessionID,<ip,SessionID>>
     * 检查Map中是否有对应的 SessionId
     *
     * @param sessionId
     * @return
     */
    public static boolean haveSessionId(String sessionId) {
        AtomicBoolean isHave = new AtomicBoolean(false);
        Jedis jedis = RedisUtil.jedisPool.getResource();
        List<String> vals = null;
        try {
            vals = jedis.hvals("iFile-SessionID");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        vals.forEach(s -> {
            if (s.equals(sessionId)) {
                isHave.set(true);
            }
        });

        return isHave.get();
    }

    /**
     * 操作数据库 Redis-Map <iFile-SessionID,<ip,SessionID>>
     * 根据SessionId修改ip
     *
     * @param key   ip
     * @param value SessionID
     */
    public static void renameIpBySessionId(String key, String value) {
        Jedis jedis = RedisUtil.jedisPool.getResource();
        AtomicReference<String> oldKey = new AtomicReference<>("");

        try {
            Map<String, String> maps = jedis.hgetAll("iFile-SessionID");
            maps.forEach((s, s2) -> {
                if (s2.equals(value)) {
                    oldKey.set(s);
                }
            });
            jedis.hdel("iFile-SessionID", oldKey.get());
            jedis.hset("iFile-SessionID", key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }
}
