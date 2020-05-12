package com.cynaith.ifile.util;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Cynaith
 **/
public class JedisClusterUtil {
    static JedisCluster jedisCluster = null;

    static {

        JedisPoolConfig config = new JedisPoolConfig();


        config.setMaxTotal(30);

        config.setMaxIdle(2);

        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();

        jedisClusterNode.add(new HostAndPort("106.12.130.1", 9001));

        jedisClusterNode.add(new HostAndPort("106.12.130.1", 9002));

        jedisClusterNode.add(new HostAndPort("106.12.130.1", 9003));

        jedisClusterNode.add(new HostAndPort("106.12.130.1", 9004));

        jedisClusterNode.add(new HostAndPort("106.12.130.1", 9005));

        jedisClusterNode.add(new HostAndPort("106.12.130.1", 9006));

        jedisCluster = new JedisCluster(jedisClusterNode,6000,1000,10,,config);
    }

    public static void set(String key,String value){
        jedisCluster.set(key, value);
    }
    public static String get(String key){
        return jedisCluster.get(key);
    }
    public static boolean check(String key,String value){
        if (value.equals(jedisCluster.get(key))){
            return true;
        }
        else return false;
    }
}
