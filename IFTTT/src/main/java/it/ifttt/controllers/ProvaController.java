package it.ifttt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.ifttt.model.user.User;
import it.ifttt.repository.UserRepository;
import it.ifttt.services.security.SecurityService;

@RestController
@RequestMapping("/prova")
public class ProvaController {
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
	private UserRepository UserRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getRecipe() {		
		return "It works!";
	}
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
    public User currentUser() {
    	User user = UserRepository.findByUsername(securityService.findLoggedInUsername());
    	return user;
    }
	
}