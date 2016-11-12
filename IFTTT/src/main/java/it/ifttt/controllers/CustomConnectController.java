package it.ifttt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

//@CrossOrigin(origins = "*")
@Controller
public class CustomConnectController extends ConnectController {
	
	@Autowired
	public CustomConnectController(ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {

		super(connectionFactoryLocator, connectionRepository);
	}
	
	@Override
	protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
		//HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		String path = "/#/loginWithSocialSuccessfull";
		return new RedirectView(path, true);
	}
	/*
	@RequestMapping(value="/{providerId}/status", method=RequestMethod.GET)
	public @ResponseBody ChannelStatusMessage getStatus(@PathVariable String providerId)
	{
		return channelService.getStatus(providerId);
	}
	
	@RequestMapping(value="/status", method=RequestMethod.GET)
	public @ResponseBody List<ChannelStatusMessage> getAllStatus()
	{
		return channelService.getAllStatus();
	}
	*/
}