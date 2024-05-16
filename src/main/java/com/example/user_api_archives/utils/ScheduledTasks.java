package com.example.user_api_archives.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.example.user_api_archives.controller.UserController;
import com.example.user_api_archives.controller.UserRemoteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private CacheManager cacheManager;
	private final UserRemoteController userRemoteController;
	private final UserController userController;

	public ScheduledTasks(
			UserRemoteController userRemoteController,
			UserController userController,
	        CacheManager cacheManager
	) {
		this.userRemoteController = userRemoteController;
		this.userController = userController;
		this.cacheManager = cacheManager;
	}

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


	@Scheduled(fixedRate = 1000)
	public void getCurrentData() throws IOException {
		var res = userController.all();
		log.info("USE CACHE " + res);
	}

	@Scheduled(fixedRate = 1000)
	public void toPutCacheActualizeData() throws IOException {
		var res =  userController.toCache();
		log.info("TO CACHE " + res);
	}
}