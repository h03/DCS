package playcount;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisOperation {
    private static JedisPool pool;

    private boolean DEBUG;

    public RedisOperation() {

    }

    public void debug(String msg) {
        System.out.println("[DEBUG]" + msg);
    }

    public RedisOperation(JedisPoolConfig config, String host, boolean debug) {
        pool = new JedisPool(host);
        DEBUG = debug;
    }

    public RedisOperation(JedisPoolConfig config, String host, int port, boolean debug) {
        DEBUG = debug;
    }

    public void redisLpushLine(String key, String member) {
        Jedis jedis = pool.getResource();
        try {
            jedis.lpush(key, member);
            if (DEBUG) {
                debug("result is " + jedis.lrange(key, 0, 0).toString());
            }
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisRpopLine(String key) {
        Jedis jedis = pool.getResource();
        try {
            String result = jedis.rpop(key);
            if (DEBUG) {
                debug("The value is " + result);
            }

        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisLpopLine(String key) {
        Jedis jedis = pool.getResource();
        try {
            String result = jedis.lpop(key);
            if (DEBUG) {
                debug("The value is " + result);
            }

        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public List<String> redisLrange(String key, long start, long end) {
        Jedis jedis = pool.getResource();
        try {
            List<String> list = jedis.lrange(key, start, end);
            return list;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }


    public void redisSaddLine(String key, String member) {
        Jedis jedis = pool.getResource();
        try {
            jedis.sadd(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisSaddGroup(String key, String[] members) {
        Jedis jedis = pool.getResource();
        try {
            jedis.sadd(key, members);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public String redisSrandMember(String key) {
        Jedis jedis = pool.getResource();
        try {
            String result = jedis.srandmember(key);
            if (DEBUG) {
                debug("The value is " + result);
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public Set<String> redisSmembers(String key) {
        Jedis jedis = pool.getResource();
        try {
            Set<String> result = jedis.smembers(key);
            if (DEBUG) {
                debug("The value is " + result);
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public void redisZaddLine(String key, String score, String member) {
        Jedis jedis = pool.getResource();
        try {
            if (!Double.valueOf(score).isNaN()) {
                jedis.zadd(key, Double.parseDouble(score), member);
            }
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisZremLine(String key, String member) {
        Jedis jedis = pool.getResource();
        try {
            jedis.zrem(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public Set<String> redisZrangeByScore(String key, String min, String max) {
        Jedis jedis = pool.getResource();
        try {
            Set<String> result = jedis.zrangeByScore(key, min, max);
            if (DEBUG) {
                StringBuffer buffer = new StringBuffer();
                for (Iterator iter = result.iterator(); iter.hasNext(); ) {
                    buffer.append(iter.next().toString() + " ");
                }
                debug("The value is " + buffer.toString());
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public Set<String> redisZrange(String key, long start, long end) {
        Jedis jedis = pool.getResource();
        try {
            Set<String> result = jedis.zrange(key, start, end);
            if (DEBUG) {
                StringBuilder builder = new StringBuilder();
                for (String s : result) {
                    builder.append(s + " ");
                }
                debug("The value is " + builder.toString());
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public String redisZscore(String key, String element) {
        Jedis jedis = pool.getResource();
        try {
            String result = jedis.zscore(key, element).toString();
            if (DEBUG) {
                debug("The score is " + result);
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public void redisZremRangeByScore(String key, String min, String max) {
        Jedis jedis = pool.getResource();
        try {
            jedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisZremRangeByRank(String key, long start, long end) {
        Jedis jedis = pool.getResource();
        try {
            jedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public Long redisZcard(String key) {
        Jedis jedis = pool.getResource();
        try {
            Long result = jedis.zcard(key);
            if (DEBUG) {
                debug("The result is " + key);
            }
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public void redisHsetLine(String key, String field, String value) {
        Jedis jedis = pool.getResource();
        try {
            jedis.hset(key, field, value);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisHmsetLine(String key, String[] fields, String[] keys) {

    }

    public void redisHmget(String key, String field) {
        Jedis jedis = pool.getResource();
        try {
            jedis.hdel(key, field);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public Long redisHlen(String key) {
        Jedis jedis = pool.getResource();
        try {
            Long num = jedis.hlen(key);
            if (DEBUG) {
                debug("The length is " + num);
            }
            return num;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public Set<String> redisHkeys(String key) {
        Jedis jedis = pool.getResource();
        try {
            Set<String> result = jedis.hkeys(key);
            if (DEBUG) {
                debug("The result is " + key);
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public List<String> redisHvals(String key) {
        Jedis jedis = pool.getResource();
        try {
            List<String> result = jedis.hvals(key);
            if (DEBUG) {
                debug("The result is " + key);
            }
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public Map<String, String> redisHgetAll(String key) {
        Jedis jedis = pool.getResource();
        try {
            Map<String, String> result = jedis.hgetAll(key);
            return result;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public void redisDelteOne(String key) {
        Jedis jedis = pool.getResource();
        try {
            jedis.del(key);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public String redisType(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.type(key);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public void redisExpireKey(String key, int seconds) {
        Jedis jedis = pool.getResource();
        try {
            jedis.expire(key, seconds);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public Long redisTTL(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.ttl(key);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return null;
    }

    public boolean redisSelectDB(int index) {
        Jedis jedis = pool.getResource();
        try {
            if (index >= 0 && index < 16) {
                jedis.select(index);
                return true;
            }
            return false;
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return false;
    }

    public boolean redisMoveKey(String key, int index) {
        Jedis jedis = pool.getResource();
        try {
            return (jedis.move(key, index) == 1);
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
        return false;
    }

    public void redisDeleteDB() {
        Jedis jedis = pool.getResource();
        try {
            jedis.flushDB();
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public void redisFlushALL() {
        Jedis jedis = pool.getResource();
        try {
            jedis.flushAll();
        } catch (Exception e) {
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
                jedis = null;
            }
        }
    }

    public static void main(String[] args) {
    }
}
