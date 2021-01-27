package webshop.authorization;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableDiscoveryClient
public class AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@Configuration
	@EnableResourceServer
	public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
					.authorizeRequests()
					/**/.anyRequest().authenticated()
					.and()
					/**/.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
			// @formatter:on
		}

	}

	@Configuration
	@EnableAuthorizationServer
	public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

		private final AuthenticationManager authenticationManager;

		@Autowired
		public AuthorizationServerConfig(AuthenticationManager authenticationManager) {
			this.authenticationManager = authenticationManager;
		}

		@Bean
		public TokenStore tokenStore() {
			return new InMemoryTokenStore();
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
			// @formatter:off
			configurer.inMemory()
					.withClient("webshop-webclient")
					.secret("secret")
					.authorizedGrantTypes("password", "refresh_token")
					.scopes("webshop-client-scope")
					.and()
					.withClient("user-service-client")
					.secret("usersecret")
					.authorizedGrantTypes("client_credentials")
					.scopes("user-service.write");
			// @formatter:on
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
			endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
		}

	}

	@Configuration
	@EnableWebSecurity
	public class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Resource(name = "userService")
		private UserDetailsService userDetailsService;

		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Autowired
		public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
		}

		@Bean
		public PasswordEncoder encoder() {
			return NoOpPasswordEncoder.getInstance();
		}

	}

}