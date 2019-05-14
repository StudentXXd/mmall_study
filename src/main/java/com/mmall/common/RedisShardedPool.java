package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;//jedis连接池

    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total",20);//最大连接数

    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle",10);//在池中最大jedis实例的空闲数

    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle",2);//在池中最小jedis实例的空闲数

    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.test.borrow",true);//在borrow（借）一个jedis实例的时候是否要进行验证操作，如果赋值true，则得到的jedis实例是可用的

    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.test.return",false);//在return（还）一个jedis实例的时候是否要进行验证操作，如果赋值true，则放回jedisPool中的jedis实例是可用的

    private static String redisIp = PropertiesUtil.getProperty("redis1.ip");

    private static Integer redisPort = PropertiesUtil.getIntegerProperty("redis1.port");

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    private static Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port");

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛异常，true阻塞直到超时，默认为true
        JedisShardInfo info = new JedisShardInfo(redisIp,redisPort,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info);
        jedisShardInfoList.add(info2);
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i=0;i<10;i++){
            jedis.set("key"+i,"value"+i);
        }
        returnResource(jedis);
//        pool.destroy();//临时调用,销毁连接池中的所有连接
        System.out.println("program is end");
    }
}
