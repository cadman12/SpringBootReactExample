package edu.pnu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.pnu.config.filter.JWTAuthenticationFilter;
import edu.pnu.config.filter.JWTAuthorizationFilter;
import edu.pnu.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationSuccessHandler oauth2SuccessHandler;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final MemberRepository memberRepository;

	@Value("${frontend.base.uri}")
	private String FRONTEND_BASE_URI; 

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		// CSRF 보호 비활성화 (CsrfFilter 제거)
		http.csrf(csrf->csrf.disable());
		http.cors(cors->cors.configurationSource(corsSource()));

		// 동일한 출처는 iframe 삽입 허용 (h2 console은 iframe으로 구성)
		http.headers(h->h.frameOptions(f->f.sameOrigin()));
		// AuthorizationFilter 등록
		http.authorizeHttpRequests(auth->auth
				.requestMatchers("/member/**").authenticated() 
				.requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll());

		// Form을 이용한 로그인을 사용하지 않겠다는 설정
		// UsernamePasswordAuthenticationFilter 제거
		http.formLogin(frmLogin->frmLogin.disable()); 

		// Http Basic인증 방식을 사용하지 않겠다는 설정
		// Authentication Header에 저장된 id:pwd를 이용하는 인증 방식
		// BasicAuthenticationFilter 제거
		http.httpBasic(basic->basic.disable());
		// SessionManagementFilter에서 이 설정을 확인
		// 개발자가 HttpSession을 요구하지 않는 한 생성하지 않는다.
		// (reqeust.getSession())
		http.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.oauth2Login(oauth2->oauth2.successHandler(oauth2SuccessHandler));
		http.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()));
		http.addFilterBefore(new JWTAuthorizationFilter(memberRepository), AuthorizationFilter.class);
		return http.build();
	}

	private CorsConfigurationSource corsSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin(FRONTEND_BASE_URI);
		config.addAllowedMethod(CorsConfiguration.ALL);
		config.addAllowedHeader(CorsConfiguration.ALL);
		config.setAllowCredentials(true);
		config.addExposedHeader(HttpHeaders.AUTHORIZATION);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source; 
	}
}
