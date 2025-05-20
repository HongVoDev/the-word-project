package com.journal.backendJournal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
			.allowedOrigins("https://thewordproject.site", "https://www.thewordproject.site"
					,"http://localhost:4200")
			.allowedMethods("POST", "OPTIONS")
			.allowedHeaders("*") 
			.allowCredentials(false)
			.maxAge(3600);
		}
	};
	}
}
