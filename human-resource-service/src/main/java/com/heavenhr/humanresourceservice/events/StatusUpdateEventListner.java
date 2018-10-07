package com.heavenhr.humanresourceservice.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StatusUpdateEventListner implements ApplicationListener<StatusUpdateEvent> {

	Logger logger = LoggerFactory.getLogger(StatusUpdateEventListner.class);
	
	@Override
	public void onApplicationEvent(StatusUpdateEvent event) {
		logger.info("Received status update for event {} where applicant {} is {}", event.getJobTitle(), event.getEmail(), event.getStatus());
	}

}
