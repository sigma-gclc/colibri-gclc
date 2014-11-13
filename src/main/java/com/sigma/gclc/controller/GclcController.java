package com.sigma.gclc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GclcController {

	static class Team {
		private List<String> members = new ArrayList<String>(5);

		public Team addMember(String string) {
			members.add(string);
			return this;
		}
	}

	@RequestMapping(value = "/gclc", produces = MediaType.APPLICATION_JSON_VALUE, consumes= MediaType.APPLICATION_JSON_VALUE)
	public Team team() {
		return new Team().addMember("myrtille").addMember("simon")
				.addMember("steve").addMember("julien").addMember("michael");
	}
}
