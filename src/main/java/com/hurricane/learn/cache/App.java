package com.hurricane.learn.cache;

import java.net.URL;
import java.util.Optional;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.xml.XmlConfiguration;
/**
 * 参考：https://blog.csdn.net/Gentlemike/article/details/80403967
 * @author Hurricane
 *
 */
public class App {

	public static void main(String[] args) throws InterruptedException {
//		cacheBaseJavaConfig();
//		cacheBaseXmlConfig();
		cacheConfigTest();
	}
	
	/**
	 * 测试ehcache配置项：1.存储空间、2.缓存过期策略 的作用
	 */
	private static void cacheConfigTest() throws InterruptedException {
		URL myUrl = App.class.getClass().getResource("/ehcache.xml"); 
		Configuration xmlConfig = new XmlConfiguration(myUrl); 
		CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
		cacheManager.init();
		
		Cache<String, String> myCache1 = cacheManager.getCache("myCache1", String.class, String.class);
		String value = "测试字符串";
		//测试配置空间不够的情况，构造大于指定空间的字符串进行储存，结果：获取到的值为null
//		for (;value.getBytes().length<1024*1024;) {
//			value += value;
//		}
		myCache1.put("attr", value);
//		System.out.println(myCache1.get("attr"));
		
		//测试缓存过期策略ttl(time to live),tti(time to idle)
		//过期时间分别设置为1s，通过下面的11次打印中，ttl情况下10次有值；tti情况下1次有值
		for (int i = 0; i < 10; i++) {
			Thread.sleep(500);
			String string = myCache1.get("attr");
			System.out.println(string);
		}
		Thread.sleep(1000);
		System.out.println(myCache1.get("attr"));
		
		cacheManager.close();
	}
	/**
	 * 使用xml配置启动ehcache
	 */
	private static void cacheBaseXmlConfig() {
		URL myUrl = App.class.getClass().getResource("/ehcache.xml"); 
		Configuration xmlConfig = new XmlConfiguration(myUrl); 
		CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
		cacheManager.init();
		
		Cache<String, String> myCache1 = cacheManager.getCache("myCache2", String.class, String.class);
		
		myCache1.put("name", "hurricane");
		System.out.println(myCache1.get("name"));
		
		cacheManager.close();		
	}
	/**
	 * 使用java配置启动ehcache
	 */
	private static void cacheBaseJavaConfig() {
		// 这一步只是实例化了CacheManager，还需要进行初始化
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				// 实例化CacheManager时，可以指定cache
				.withCache("myCache1", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)))
				.build();
		// CacheManager初始化,也可以在上面的build方法中传入参数true，来达到初始化的目的
		cacheManager.init();

		Cache<Long, String> myCache1 = cacheManager.getCache("myCache1", Long.class, String.class);
		// ehcache也可以使用这种方式构造一个cache，cache是不可以重名的
		Cache<String, String> myCache2 = cacheManager.createCache("myCache2", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10)));
		myCache1.put(0L, "tornado");
		myCache2.put("name","hurricane");
		System.out.println(myCache1.get(0L));
		System.out.println(myCache2.get("name"));
		// 可以用这种方式移除一个已经存在的cache
		cacheManager.removeCache("myCache1");
		// 关闭所有的cache
		cacheManager.close();		
	}

}
