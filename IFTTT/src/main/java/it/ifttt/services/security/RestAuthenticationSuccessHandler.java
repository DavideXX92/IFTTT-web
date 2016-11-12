package it.ifttt.services.security;

import it.ifttt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	/*
    @Autowired
    private UserInfoRepository userInfoRepository;
    */
    @Autowired
    private UserRepository UserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
    	// TODO il login al momento lo fa con spring social quindi qui ritorna le info social
    	// ma deve essere fatto con l'account normale dell'utente.
    	/*
        UserInfo user = userInfoRepository.findByUserSocialId(authentication.getName());
        */
    	/*User user = */UserRepository.findByUsername(authentication.getName());
        SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, null);
    }
}