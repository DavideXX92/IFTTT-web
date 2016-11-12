package it.ifttt.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.ifttt.repository.UserConnectionRepository;
import it.ifttt.social.CalendarCreator;
import it.ifttt.social.TwitterTemplateCreator;

@Controller
@RequestMapping("/gui")
public class IftttController {
	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;
	
	@RequestMapping(value={"", "/", "home"})
	public String home(Model model){		
		return "index";
	}	
	
	@RequestMapping("/prova")
	public String testSocial(HttpServletRequest request){
		
		return "prova_success";
	}
}
