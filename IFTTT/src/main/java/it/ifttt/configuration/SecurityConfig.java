package it.ifttt.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
// @EnableJpaRepositories
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("it.ifttt.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// @Autowired
	// private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler restAuthenticationFailureHandler;
	
	@Autowired
	private LogoutSuccessHandler restLogoutSuccessHandler;


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		 * auth .jdbcAuthentication() .dataSource(dataSource)
		 * .usersByUsernameQuery(
		 * "SELECT username,password,enabled FROM utente WHERE username=?")
		 * .authoritiesByUsernameQuery(
		 * "SELECT U.username,role FROM ruolo R, utente U WHERE U.username=? AND U.id=R.username"
		 * );
		 *//*
		auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER").and().withUser("admin")
				.password("admin").roles("USER", "ADMIN");

	}
*/
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.headers().disable()
			.csrf().disable()

			.authenticationProvider(authenticationProvider())

			.authorizeRequests()
				.antMatchers("/", "/home.html").permitAll()
				.antMatchers("/**").hasRole("ADMIN")
				.antMatchers("/**").hasRole("USER")
				.antMatchers("/**").authenticated()
				.and()
			.formLogin()
				.successHandler(restAuthenticationSuccessHandler)
	            .failureHandler(restAuthenticationFailureHandler)
	            .usernameParameter("username")
	            .passwordParameter("password")
	            .permitAll()
				.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(restLogoutSuccessHandler)
				.deleteCookies("JSESSIONID")
				.permitAll();
		// .usernameParameter("user")
		// default is username
		// .passwordParameter("pass")
		// default is password
		// .loginPage("/auth/login")
		// default is /login with an HTTP get
		// .failureUrl("/auth/login?failed");
		// default is /login?error
		// .loginProcessingUrl("/auth/login/process")
		// default is /login with an HTTP post
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// configura la catena dei filtri di sicurezza
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		//authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

		return authenticationProvider;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
