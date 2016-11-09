package it.ifttt.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
 
	@Override
	protected Class<?>[] getRootConfigClasses() {
		// Contesto Spring per servizi e basi dati		
		return new Class[]{RootConfig.class};
	}
	@Override
	protected Class<?>[] getServletConfigClasses() {
       // Contesto Spring per la parte web
		return new Class[]{WebConfig.class};
	}
	@Override
	protected String[] getServletMappings() {
       // tipi di file su cui Spring opera (“/” indica tutti)
		return new String[]{"/"};
	}
}
