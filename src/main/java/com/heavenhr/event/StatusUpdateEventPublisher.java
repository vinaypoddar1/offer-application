package com.heavenhr.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class StatusUpdateEventPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	public void doStuffAndPublishAnEvent(final String message) {
        System.out.println("Publishing custom event. ");
        StatusUpdateEvent statusUpdateEvent = new StatusUpdateEvent(this);
        applicationEventPublisher.publishEvent(statusUpdateEvent);
    }
}
