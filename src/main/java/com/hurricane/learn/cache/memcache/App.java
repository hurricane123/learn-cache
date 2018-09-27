package com.hurricane.learn.cache.memcache;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.hurricane.learn.cache.entity.User;

import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

public class App {

	public static void main(String[] args){
		MemcachedClient client = null;
		try {
			client = new MemcachedClient(new InetSocketAddress("192.168.137.100", 11211));
			User user = new User();
			user.setUsername("hurricane");
			user.setAge(12);
			//连接客户端
			//add
			client.add("user", 0, user);
			Object getUser = client.get("user");
			if (getUser instanceof User) {
				System.out.println(getUser);
			}
			//set
			client.set("user2", 0, user);
			getUser = client.get("user2");
			if (getUser instanceof User) {
				System.out.println(getUser);
			}
			//append prepend
			client.set("str", 0, "aaa");
			CASValue<Object> casToken = client.gets("str");
			System.out.println(casToken.getCas());
			client.append(casToken.getCas(), "str", "bbb");
			casToken = client.gets("str");
			System.out.println(casToken.getCas());
			client.prepend(casToken.getCas(), "str", "ccc");
			System.out.println(client.get("str"));
			//replace
			client.replace("str", 0, "hhh");
			System.out.println(client.get("str"));
			//delete
			client.delete("str");
			System.out.println(client.get("str"));
			//incr decr无法正常执行，因为保存到memcache的过程中java将int基本类型转成了Integer对象
			client.set("age", 0, 1111110L);
//			client.incr("age", 3);
//			client.decr("age", 3);
			System.out.println(client.get("age").getClass());
		} catch (Exception e) {
			System.out.println("发生错误");
		}finally {
			if (client!=null) {
				//关闭客户端
				client.shutdown();
				client=null;
			}
		}




	}
}
