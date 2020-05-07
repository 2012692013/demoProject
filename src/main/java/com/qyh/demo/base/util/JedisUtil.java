package com.qyh.demo.base.util;

import com.qyh.demo.constants.ContextConstants;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IDEA
 * author:huxi
 * Date:2018/11/25
 * Time:2:50 PM
 */
@Configuration
public class JedisUtil {

    public static JedisPool pool = null;

//    public static Subscriber subscriber = new Subscriber();

    public static JedisPool init(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(4);
        jedisPoolConfig.setMaxIdle(4);
        jedisPoolConfig.setMaxWaitMillis(-1);
        jedisPoolConfig.setTestOnBorrow(false);
        if (StringUtils.isNotBlank(ContextConstants.REDIS_PASSWORD)) {
            // redis 设置了密码
            pool = new JedisPool(jedisPoolConfig, ContextConstants.REDIS_HOST, ContextConstants.REDIS_PORT, 10000, ContextConstants.REDIS_PASSWORD);
        } else {
            // redis 未设置密码
            pool = new JedisPool(jedisPoolConfig, ContextConstants.REDIS_HOST, ContextConstants.REDIS_PORT, 10000);
        }
        return pool;
    }




    /**
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public static String get(String key, int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        String value = null;
        value = jedis.get(key);
        jedis.close();
        return value;
    }

    /**
     * 设置key的值为value
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value, int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        String r = jedis.set(key, value);
        jedis.close();
        return r;
    }

    //对象set与get

    public static Object getObject(String key, int index){
        Jedis jedis = getJedis();
        jedis.select(index);
        Object result = SerializationUtil.deserialize(jedis.get(key.getBytes()));
        jedis.close();
        return result;
    }

    public static String setObj(String key, Object value, int index){
        Jedis jedis = getJedis();
        jedis.select(index);
        String result = jedis.set(key.getBytes(), SerializationUtil.serialize(value));
        jedis.close();
        return result;
    }

    /**
     * 删除指定的key,也可以传入一个包含key的数组
     *
     * @param keys
     * @return
     */
    public static Long del(String... keys) {
        Jedis jedis = getJedis();
        Long del = jedis.del(keys);
        jedis.close();
        return del;
    }

    /**
     * 根据传入的库删除对应元素
     * @param key
     * @return
     */
    public static Long del(String key, int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        Long del = jedis.del(key);
        jedis.close();
        return del;
    }

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    public static Long append(String key, String str) {
        Jedis jedis = getJedis();
        Long append = jedis.append(key, str);
        jedis.close();
        return append;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        Jedis jedis = getJedis();
        Boolean exists = jedis.exists(key);
        jedis.close();
        return exists;
    }

    public static Boolean exists(String key,int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        Boolean exists = jedis.exists(key);
        jedis.close();
        return exists;
    }

    /**
     * 设置key value,如果key已经存在则返回0
     *
     * @param key
     * @param value
     * @return
     */
    public static Long setnx(String key, String value) {
        Jedis jedis = getJedis();
        Long setnx = jedis.setnx(key, value);
        jedis.close();
        return setnx;
    }

    /**
     * 设置key value并指定这个键值的有效期
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public static String setex(String key, int seconds, String value) {
        Jedis jedis = getJedis();
        String setex = jedis.setex(key, seconds, value);
        jedis.close();
        return setex;
    }

    /**
     * 设置key value并指定这个键值的有效期
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public static String setex(String key, int seconds, String value,int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        String setex = jedis.setex(key, seconds, value);
        jedis.close();
        return setex;
    }

    /**
     * 通过key 和offset 从指定的位置开始将原先value替换
     *
     * @param key
     * @param offset
     * @param str
     * @return
     */
    public static Long setrange(String key, int offset, String str) {
        Jedis jedis = getJedis();
        Long setrange = jedis.setrange(key, offset, str);
        jedis.close();
        return setrange;
    }

    /**
     * 通过批量的key获取批量的value
     *
     * @param keys
     * @return
     */
    public static List<String> mget(String... keys) {
        Jedis jedis = getJedis();
        List<String> mget = jedis.mget(keys);
        jedis.close();
        return mget;
    }

    /**
     * 批量的设置key:value,也可以一个
     *
     * @param keysValues
     * @return
     */
    public static String mset(String... keysValues) {
        Jedis jedis = getJedis();
        String mset = jedis.mset(keysValues);
        jedis.close();
        return mset;
    }

    /**
     * 批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚
     *
     * @param keysValues
     * @return
     */
    public static Long msetnx(String... keysValues) {
        Jedis jedis = getJedis();
        Long msetnx = jedis.msetnx(keysValues);
        jedis.close();
        return msetnx;
    }

    /**
     * 设置key的值,并返回一个旧值
     *
     * @param key
     * @param value
     * @return
     */
    public static String getSet(String key, String value) {
        Jedis jedis = getJedis();
        String set = jedis.getSet(key, value);
        jedis.close();
        return set;
    }

    /**
     * 通过下标 和key 获取指定下标位置的 value
     *
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public static String getrange(String key, int startOffset, int endOffset) {
        Jedis jedis = getJedis();
        String getrange = jedis.getrange(key, startOffset, endOffset);
        jedis.close();
        return getrange;
    }

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     *
     * @param key
     * @return
     */
    public static Long incr(String key) {
        Jedis jedis = getJedis();
        Long incr = jedis.incr(key);
        jedis.close();
        return incr;
    }

    /**
     * 通过key给指定的value加值,如果key不存在,则这是value为该值
     *
     * @param key
     * @param integer
     * @return
     */
    public static Long incrBy(String key, long integer) {
        Jedis jedis = getJedis();
        Long aLong = jedis.incrBy(key, integer);
        jedis.close();
        return aLong;
    }

    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1
     *
     * @param key
     * @return
     */
    public static Long decr(String key) {
        Jedis jedis = getJedis();
        Long decr = jedis.decr(key);
        jedis.close();
        return decr;
    }

    /**
     * 减去指定的值
     *
     * @param key
     * @param integer
     * @return
     */
    public static Long decrBy(String key, long integer) {
        Jedis jedis = getJedis();
        Long aLong = jedis.decrBy(key, integer);
        jedis.close();
        return aLong;
    }

    /**
     * 通过key获取value值的长度
     *
     * @param key
     * @return
     */
    public static Long strLen(String key) {
        Jedis jedis = getJedis();
        Long strlen = jedis.strlen(key);
        jedis.close();
        return strlen;
    }

    /**
     * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hsetnx(String key, String field, String value) {
        Jedis jedis = getJedis();
        Long hsetnx = jedis.hsetnx(key, field, value);
        jedis.close();
        return hsetnx;
    }

    /**
     * 通过key给field设置指定的值,如果key不存在,则先创建
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hset(String key, String field, String value) {
        Jedis jedis = getJedis();
        Long hset = jedis.hset(key, field, value);
        jedis.close();
        return hset;
    }

    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public static String hmset(String key, Map<String, String> hash) {
        Jedis jedis = getJedis();
        String hmset = jedis.hmset(key, hash);
        jedis.close();
        return hmset;
    }

    /**
     * 通过key 和 field 获取指定的 value
     *
     * @param key
     * @param failed
     * @return
     */
    public static String hget(String key, String failed) {
        Jedis jedis = getJedis();
        String hget = jedis.hget(key, failed);
        jedis.close();
        return hget;
    }

    /**
     * 设置key的超时时间为seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    public static Long expire(String key, int seconds) {
        Jedis jedis = getJedis();
        Long expire = jedis.expire(key, seconds);
        jedis.close();
        return expire;
    }

    public static Long expire(String key, int seconds, int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        Long expire = jedis.expire(key, seconds);
        jedis.close();
        return expire;
    }

    /**
     * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
     *
     * @param key
     * @param fields 可以是 一个String 也可以是 String数组
     * @return
     */
    public static List<String> hmget(String key, String... fields) {
        Jedis jedis = getJedis();
        List<String> hmget = jedis.hmget(key, fields);
        jedis.close();
        return hmget;
    }

    /**
     * 通过key给指定的field的value加上给定的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hincrby(String key, String field, Long value) {
        Jedis jedis = getJedis();
        Long aLong = jedis.hincrBy(key, field, value);
        jedis.close();
        return aLong;
    }

    /**
     * 通过key和field判断是否有指定的value存在
     *
     * @param key
     * @param field
     * @return
     */
    public static Boolean hexists(String key, String field) {
        Jedis jedis = getJedis();
        Boolean hexists = jedis.hexists(key, field);
        jedis.close();
        return hexists;
    }

    /**
     * 通过key返回field的数量
     *
     * @param key
     * @return
     */
    public static Long hlen(String key) {
        Jedis jedis = getJedis();
        Long hlen = jedis.hlen(key);
        jedis.close();
        return hlen;
    }

    /**
     * 通过key 删除指定的 field
     *
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public static Long hdel(String key, String... fields) {
        Jedis jedis = getJedis();
        Long hdel = jedis.hdel(key, fields);
        jedis.close();
        return hdel;
    }

    /**
     * 通过key返回所有的field
     *
     * @param key
     * @return
     */
    public static Set<String> hkeys(String key) {
        Jedis jedis = getJedis();
        Set<String> hkeys = jedis.hkeys(key);
        jedis.close();
        return hkeys;
    }

    /**
     * 通过key返回所有和key有关的value
     *
     * @param key
     * @return
     */
    public static List<String> hvals(String key) {
        Jedis jedis = getJedis();
        List<String> hvals = jedis.hvals(key);
        jedis.close();
        return hvals;
    }

    /**
     * 通过key获取所有的field和value
     *
     * @param key
     * @return
     */
    public static Map<String, String> hgetall(String key) {
        Jedis jedis = getJedis();
        Map<String, String> stringStringMap = jedis.hgetAll(key);
        jedis.close();
        return stringStringMap;
    }

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public static Long lpush(String key, String... strs) {
        Jedis jedis = getJedis();
        Long lpush = jedis.lpush(key, strs);
        jedis.close();
        return lpush;
    }

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public static Long rpush(String key, String... strs) {
        Jedis jedis = getJedis();
        Long rpush = jedis.rpush(key, strs);
        jedis.close();
        return rpush;
    }

    /**
     * 通过key在list指定的位置之前或者之后 添加字符串元素
     *
     * @param key
     * @param where LIST_POSITION枚举类型
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public static Long linsert(String key, BinaryClient.LIST_POSITION where,
                               String pivot, String value) {
        Jedis jedis = getJedis();
        Long linsert = jedis.linsert(key, where, pivot, value);
        jedis.close();
        return linsert;
    }

    /**
     * 通过key设置list指定下标位置的value
     * 如果下标超过list里面value的个数则报错
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public static String lset(String key, Long index, String value) {
        Jedis jedis = getJedis();
        String lset = jedis.lset(key, index, value);
        jedis.close();
        return lset;
    }

    /**
     * 通过key从对应的list中删除指定的count个 和 value相同的元素
     *
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public static Long lrem(String key, long count, String value) {
        Jedis jedis = getJedis();
        Long lrem = jedis.lrem(key, count, value);
        jedis.close();
        return lrem;
    }

    /**
     * 通过key保留list中从strat下标开始到end下标结束的value值
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public static String ltrim(String key, long start, long end) {
        Jedis jedis = getJedis();
        String ltrim = jedis.ltrim(key, start, end);
        jedis.close();
        return ltrim;
    }

    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public static synchronized String lpop(String key) {
        Jedis jedis = getJedis();
        String lpop = jedis.lpop(key);
        jedis.close();
        return lpop;
    }

    /**
     * 通过key从list尾部删除一个value,并返回该元素
     *
     * @param key
     * @return
     */
    synchronized public static String rpop(String key) {
        Jedis jedis = getJedis();
        String rpop = jedis.rpop(key);
        jedis.close();
        return rpop;
    }

    /**
     * 通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value
     * 如果第一个list为空或者不存在则返回null
     *
     * @param srckey
     * @param dstkey
     * @return
     */
    public static String rpoplpush(String srckey, String dstkey) {
        Jedis jedis = getJedis();
        String rpoplpush = jedis.rpoplpush(srckey, dstkey);
        jedis.close();
        return rpoplpush;
    }

    /**
     * 通过key获取list中指定下标位置的value
     *
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public static String lindex(String key, long index) {
        Jedis jedis = getJedis();
        String lindex = jedis.lindex(key, index);
        jedis.close();
        return lindex;
    }

    /**
     * 通过key返回list的长度
     *
     * @param key
     * @return
     */
    public static Long llen(String key) {
        Jedis jedis = getJedis();
        Long llen = jedis.llen(key);
        jedis.close();
        return llen;
    }

    /**
     * 通过key获取list指定下标位置的value
     * 如果start 为 0 end 为 -1 则返回全部的list中的value
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<String> lrange(String key, long start, long end) {
        Jedis jedis = getJedis();
        List<String> lrange = jedis.lrange(key, start, end);
        jedis.close();
        return lrange;
    }

    /**
     * 通过key向指定的set中添加value
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public static Long sadd(String key, String... members) {
        Jedis jedis = getJedis();
        Long sadd = jedis.sadd(key, members);
        jedis.close();
        return sadd;
    }

    /**
     * 通过key删除set中对应的value值
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public static Long srem(String key, String... members) {
        Jedis jedis = getJedis();
        Long srem = jedis.srem(key, members);
        jedis.close();
        return srem;
    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key
     * @return
     */
    public static String spop(String key) {
        Jedis jedis = getJedis();
        String spop = jedis.spop(key);
        jedis.close();
        return spop;
    }

    /**
     * 通过key获取set中的差集
     * 以第一个set为标准
     *
     * @param keys 可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public static Set<String> sdiff(String... keys) {
        Jedis jedis = getJedis();
        Set<String> sdiff = jedis.sdiff(keys);
        jedis.close();
        return sdiff;
    }

    /**
     * 通过key获取set中的差集并存入到另一个key中
     * 以第一个set为标准
     *
     * @param dstkey 差集存入的key
     * @param keys   可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public static Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = getJedis();
        Long sdiffstore = jedis.sdiffstore(dstkey, keys);
        jedis.close();
        return sdiffstore;
    }

    /**
     * 通过key获取指定set中的交集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Set<String> sinter(String... keys) {
        Jedis jedis = getJedis();
        Set<String> sinter = jedis.sinter(keys);
        jedis.close();
        return sinter;
    }

    /**
     * 通过key获取指定set中的交集 并将结果存入新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = getJedis();
        Long sinterstore = jedis.sinterstore(dstkey, keys);
        jedis.close();
        return sinterstore;
    }

    /**
     * 通过key返回所有set的并集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Set<String> sunion(String... keys) {
        Jedis jedis = getJedis();
        Set<String> sunion = jedis.sunion(keys);
        jedis.close();
        return sunion;
    }

    /**
     * 通过key返回所有set的并集,并存入到新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = getJedis();
        Long sunionstore = jedis.sunionstore(dstkey, keys);
        jedis.close();
        return sunionstore;
    }

    /**
     * 通过key将set中的value移除并添加到第二个set中
     *
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     * @return
     */
    public static Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = getJedis();
        Long smove = jedis.smove(srckey, dstkey, member);
        jedis.close();
        return smove;
    }

    /**
     * 通过key获取set中value的个数
     *
     * @param key
     * @return
     */
    public static Long scard(String key) {
        Jedis jedis = getJedis();
        Long scard = jedis.scard(key);
        jedis.close();
        return scard;
    }

    /**
     * 通过key判断value是否是set中的元素
     *
     * @param key
     * @param member
     * @return
     */
    public static Boolean sismember(String key, String member) {
        Jedis jedis = getJedis();
        Boolean sismember = jedis.sismember(key, member);
        jedis.close();
        return sismember;
    }

    /**
     * 通过key获取set中随机的value,不删除元素
     *
     * @param key
     * @return
     */
    public static String srandmember(String key) {
        Jedis jedis = getJedis();
        String srandmember = jedis.srandmember(key);
        jedis.close();
        return srandmember;
    }

    /**
     * 通过key获取set中所有的value
     *
     * @param key
     * @return
     */
    public static Set<String> smembers(String key) {
        Jedis jedis = getJedis();
        Set<String> smembers = jedis.smembers(key);
        jedis.close();
        return smembers;
    }


    /**
     * 通过key向zset中添加value,score,其中score就是用来排序的
     * 如果该value已经存在则根据score更新元素
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public static Long zadd(String key, double score, String member) {
        Jedis jedis = getJedis();
        Long zadd = jedis.zadd(key, score, member);
        jedis.close();
        return zadd;
    }

    /**
     * 通过key删除在zset中指定的value
     *
     * @param key
     * @param members 可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Long zrem(String key, String... members) {
        Jedis jedis = getJedis();
        Long zrem = jedis.zrem(key, members);
        jedis.close();
        return zrem;
    }

    /**
     * 通过key增加该zset中value的score的值
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public static Double zincrby(String key, double score, String member) {
        Jedis jedis = getJedis();
        Double zincrby = jedis.zincrby(key, score, member);
        jedis.close();
        return zincrby;
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从小到大排序
     *
     * @param key
     * @param member
     * @return
     */
    public static Long zrank(String key, String member) {
        Jedis jedis = getJedis();
        Long zrank = jedis.zrank(key, member);
        jedis.close();
        return zrank;
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从大到小排序
     *
     * @param key
     * @param member
     * @return
     */
    public static Long zrevrank(String key, String member) {
        Jedis jedis = getJedis();
        Long zrevrank = jedis.zrevrank(key, member);
        jedis.close();
        return zrevrank;
    }

    /**
     * 通过key将获取score从start到end中zset的value
     * socre从大到小排序
     * 当start为0 end为-1时返回全部
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = getJedis();
        Set<String> zrevrange = jedis.zrevrange(key, start, end);
        jedis.close();
        return zrevrange;
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public static Set<String> zrangebyscore(String key, String max, String min) {
        Jedis jedis = getJedis();
        Set<String> strings = jedis.zrevrangeByScore(key, max, min);
        jedis.close();
        return strings;
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public static Set<String> zrangeByScore(String key, double max, double min) {
        Jedis jedis = getJedis();
        Set<String> strings = jedis.zrevrangeByScore(key, max, min);
        jedis.close();
        return strings;
    }

    /**
     * 返回指定区间内zset中value的数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long zcount(String key, String min, String max) {
        Jedis jedis = getJedis();
        Long zcount = jedis.zcount(key, min, max);
        jedis.close();
        return zcount;
    }

    /**
     * 通过key返回zset中的value个数
     *
     * @param key
     * @return
     */
    public static Long zcard(String key) {
        Jedis jedis = getJedis();
        Long zcard = jedis.zcard(key);
        jedis.close();
        return zcard;
    }

    /**
     * 通过key获取zset中value的score值
     *
     * @param key
     * @param member
     * @return
     */
    public static Double zscore(String key, String member) {
        Jedis jedis = getJedis();
        Double zscore = jedis.zscore(key, member);
        jedis.close();
        return zscore;
    }

    /**
     * 通过key删除给定区间内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = getJedis();
        Long aLong = jedis.zremrangeByRank(key, start, end);
        jedis.close();
        return aLong;
    }

    /**
     * 通过key删除指定score内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = getJedis();
        Long aLong = jedis.zremrangeByScore(key, start, end);
        jedis.close();
        return aLong;
    }

    /**
     * 返回满足pattern表达式的所有key
     * keys(*)
     * 返回所有的key
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
        Jedis jedis = getJedis();
        Set<String> keys = jedis.keys(pattern);
        jedis.close();
        return keys;
    }

    /**
     * 返回满足pattern表达式的所有key
     * keys(*)
     * 返回所有的key
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern,Integer index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        Set<String> keys = jedis.keys(pattern);
        jedis.close();
        return keys;
    }

    /**
     * 通过key判断值得类型
     *
     * @param key
     * @return
     */
    public static String type(String key) {
        Jedis jedis = getJedis();
        String type = jedis.type(key);
        jedis.close();
        return type;
    }

    public static Jedis getJedis() {
        if (pool==null)
            init();
        return pool.getResource();
    }
    public Jedis getJedisPub() {
        if (pool==null)
            init();
        return pool.getResource();
    }

    public static JedisUtil getJedisUtil() {
        return new JedisUtil();
    }

    public static JedisPool getJedisPool(){
        return pool==null?new JedisUtil().pool:pool;
    }


}
