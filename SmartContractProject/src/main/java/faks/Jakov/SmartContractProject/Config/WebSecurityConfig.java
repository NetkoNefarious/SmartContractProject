package faks.Jakov.SmartContractProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override // Route security
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            	.antMatchers("/css/**", "/bootstrap/**", "/images/**", "/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");
    }

    @Override // Authentication
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	String ENCODED_PASSWORD // password
    		= "$2a$12$M5tWOEy.7m3nyDVftiXC/uRAQgvujmBCbKlSk5t.SY4j/Nfixw2hO";
    	
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder())
			.withUser("user").password(ENCODED_PASSWORD).roles("USER");
    }
    
    @Bean // Provides a password encoder
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}
