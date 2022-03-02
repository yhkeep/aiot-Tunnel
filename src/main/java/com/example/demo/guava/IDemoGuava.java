package com.example.demo.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
/** 
 * 该接口的实现被认为是线程安全的，即可在多线程中调用 
 * 通过被定义单例使用 
 */
public interface IDemoGuava<K, V> {  
	 
	  /** 
	   * 通过key获取缓存中的value，若不存在直接返回null 
	   */  
	  V getIfPresent(Object key);  
	 
	  /** 
	   * 通过key获取缓存中的value，若不存在就通过valueLoader来加载该value 
	   * 整个过程为 "if cached, return; otherwise create, cache and return" 
	   * 注意valueLoader要么返回非null值，要么抛出异常，绝对不能返回null 
	   */  
	  V get(K key, Callable<? extends V> valueLoader) throws ExecutionException;  
	 
	  /** 
	   * 添加缓存，若key存在，就覆盖旧值 
	   */  
	  void put(K key, V value);  
	 
	  /** 
	   * 删除该key关联的缓存 
	   */  
	  void invalidate(Object key);  
	 
	  /** 
	   * 删除所有缓存 
	   */  
	  void invalidateAll();  
	 
	  /** 
	   * 执行一些维护操作，包括清理缓存 
	   */  
	  void cleanUp();  
	}