package com.heavenhr.humanresourceservice.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.heavenhr.humanresourceservice.entities.Application;

@Deprecated
//@Repository
public class ApplicationRepository {
	
	private Map<String, Application> applications;
	
	public ApplicationRepository () {
		applications = new HashMap<>();
		
	}

}
