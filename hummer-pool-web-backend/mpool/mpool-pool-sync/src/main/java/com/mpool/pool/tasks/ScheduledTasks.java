package com.mpool.pool.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mpool.common.model.account.Blockchain;

@Component
public class ScheduledTasks {
	@Autowired
	private RestTemplate restTemplate;
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	//@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		ResponseEntity<Blockchain> forEntity = restTemplate.getForEntity("http://localhost:8080/js/b.json",
				Blockchain.class);
		forEntity.getBody();
		log.debug("{}", forEntity);
		log.info("The time is now {}", dateFormat.format(new Date()));
	}

}
