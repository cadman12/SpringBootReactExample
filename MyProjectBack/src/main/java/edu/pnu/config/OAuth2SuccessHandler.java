package edu.pnu.config;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.pnu.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	public static Map<String, String> tempCodeStore = new ConcurrentHashMap<>();
//	private final StringRedisTemplate redisTemplate;

	@Value("${frontend.base.uri}")
	private String FRONTEND_BASE_URI;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
							HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
		OAuth2AuthenticationToken authToken =
														(OAuth2AuthenticationToken)authentication;
		// google, naver 등
		String provider = authToken.getAuthorizedClientRegistrationId();

		Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
		String email = "";
		if ("google".equals(provider)) {
			email = (String) attributes.get("email");
		} else if ("github".equals(provider)) {
			// email이 null이면 ID 활용
			email = (attributes.get("email") != 	null)?
			attributes.get("email").toString():attributes.get("id")+"@github.com"; 
		} else if ("naver".equals(provider)) {
			Map<String, Object> account =
												(Map<String, Object>) attributes.get("response");
			email = (String) account.get("email");
		}
		String token = JWTUtil.getJWT(provider + "_" + email); // JWT 생성
		System.out.println("[OAuth2SuccessHandler]token:" + token);
		// 1. 임시 코드 생성 (UUID)
		String code = UUID.randomUUID().toString();
		// 2. 인증 정보 임시 보관
		tempCodeStore.put(code, token);
//		redisTemplate.opsForValue().set(code, token);
		// 프런트(React)의 callback 호출
		response.sendRedirect(FRONTEND_BASE_URI + "/myprojectfront/#/callback?code=" + code);
	}
}
