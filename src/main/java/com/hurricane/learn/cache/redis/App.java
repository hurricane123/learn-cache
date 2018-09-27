package com.hurricane.learn.cache.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hurricane.learn.cache.entity.User;
import com.hurricane.learn.cache.utils.ObjectUtil;

import redis.clients.jedis.Jedis;

/**
 * 对redis进行的简单操作
 * 环境搭建参见：https://blog.csdn.net/hurricane_li/article/details/82867943
 * @author lijm11
 */
public class App {
	
	
	public static void main(String[] args) {
		Jedis jedis = null;
		try {
			jedis = new Jedis("192.168.137.100", 6379);
			String ping = jedis.ping();
			System.out.println(ping);
			//string
			jedis.set("string", "hurricane");
			String string = jedis.get("string");
			System.out.println(string);
			//list
			jedis.lpush("list", "aaa","bbb","ccc");
			List<String> lrange = jedis.lrange("list", 0, 10);
			System.out.println(lrange);
			//hash
			Map<String, String> hash = new HashMap<>();
			hash.put("name", "hurricane");
			hash.put("age", "12");
			jedis.hmset("hash", hash);
			jedis.hset("hash", "password", "123");
			Map<String, String> hgetAll = jedis.hgetAll("hash");
			System.out.println(hgetAll);
			//set
			jedis.sadd("set", "s1","s2","s3","s1");
			Set<String> smembers = jedis.smembers("set");
			System.out.println(smembers);
			//zset
			Map<String, Double> scoreMembers = new HashMap<>();
			scoreMembers.put("zs1", 1D);
			scoreMembers.put("zs3", 3D);
			scoreMembers.put("zs2", 2D);
			jedis.zadd("zset", scoreMembers);
			Set<String> zrange = jedis.zrange("zset", 0, 3);
			System.out.println(zrange);
			//保存对象
			User user = new User();
			user.setUsername("hurricane");
			user.setAge(12);
			jedis.set("user".getBytes(), ObjectUtil.objToByte(user));
			byte[] bs = jedis.get("user".getBytes());
			User user2 = (User) ObjectUtil.byteToObj(bs);
			System.out.println(user2);
			
			//其他命令
//			jedis.keys("*");
//			jedis.exists("user");
//			jedis.expire("set", 30);
//			jedis.ttl("set");
		} catch (Exception e) {
			System.out.println("发生了错误");
			e.printStackTrace();
		}finally {
			if (jedis!=null) {
				jedis.close();
				jedis = null;
			}
		}
	}
}
