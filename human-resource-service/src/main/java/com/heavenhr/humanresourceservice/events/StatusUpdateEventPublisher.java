package com.heavenhr.humanresourceservice.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class StatusUpdateEventPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	Logger logger = LoggerFactory.getLogger(StatusUpdateEventListner.class);
	
	public void doStuffAndPublishAnEvent(final String message) {
		logger.info("Publishing custom event. ");
        StatusUpdateEvent statusUpdateEvent = new StatusUpdateEvent(this);
        applicationEventPublisher.publishEvent(statusUpdateEvent);
    }
}
