package it.ifttt.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class IftttController {		
	@Autowired
	private Twitter twitter;
	
	@RequestMapping("/home")
	public String home(Model model){		
		return "index";
	}
	
	@RequestMapping("/prova")
	public String testSocial(HttpServletRequest request){
		List<Tweet> tweet = twitter.timelineOperations().getUserTimeline("Snitecs");
		for(Tweet t:tweet)
			System.out.println(t.getText());
		return "prova_success";
	}
}
