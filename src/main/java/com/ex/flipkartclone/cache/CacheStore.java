package com.ex.flipkartclone.cache;

import java.time.Duration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

//  1st to create cache using Google guava(com.google.guava)

public class CacheStore<T> {

	private Cache<String, T> cache;

	public CacheStore(Duration expiry) {
		this.cache = CacheBuilder.newBuilder().expireAfterWrite(expiry)
				.concurrencyLevel(Runtime.getRuntime().availableProcessors()).build();
	}

	
//  Methods to add/get/remove cache 
	
	public void add(String key, T value) {
		cache.put(key, value);

	}

	public T get(String key) {
		return cache.getIfPresent(key);

	}
	
	public void remove(String key) {
		cache.invalidate(key);
	}
}
