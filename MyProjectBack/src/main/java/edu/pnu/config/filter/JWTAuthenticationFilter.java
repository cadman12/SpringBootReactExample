package edu.pnu.config.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.domain.Member;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthenticationFilter
												extends UsernamePasswordAuthenticationFilter {
	// 인증을 실행하는 객체 (ProviderManager 구현체)
	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
					HttpServletResponse response) throws AuthenticationException {
		System.out.println("attemptAuthentication");
		ObjectMapper mapper = new ObjectMapper(); // JSON/Object Mapping 객체 생성
		Member member = null;
		try {
			// request에서 [username/password]를 읽어서 Member 객체를 생성
			member = mapper.readValue(request.getInputStream(), Member.class);
			System.out.println("==>" + member);
		} catch (IOException e) {
			return null; // 예외 발생 시 null 리턴 : unsuccessfulAuthentication 호출
		}
		if (member == null)
			return null; // null 리턴 : unsuccessfulAuthentication 호출

		// Security에게 자격 증명 요청에 필요한 객체 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(
														member.getUsername(), member.getPassword());
		// 인증 메서드 호출 : UserDetailsService의 loadUserByUsername에서 DB로부터
		// 사용자 정보를 읽어온 뒤 사용자 입력 정보(member)와 비교 검증
		// 자격 증명에 성공하면 Authenticaiton객체를 만들어서 리턴하면
 		// successfulAuthentication 호출, 실패하면 unsuccessfulAuthentication 호출
		return authenticationManager.authenticate(authToken);
	}
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
				HttpServletResponse response, FilterChain chain,
				Authentication authResult) throws IOException, ServletException {

		// loadUserByUsername에서 만든 객체가 authResult에 담겨져 있다.
		User user = (User)authResult.getPrincipal(); 
		System.out.println("[JWTAuthenticationFilter]auth:" + user);

		// username으로 JWT 생성. 이것은 예시로서 필요에 따라 정보 추가
		String token = JWTUtil.getJWT(user.getUsername());
		// Response Header[Authorization]에 JWT를 저장해서 응답한다.
		response.addHeader(HttpHeaders.AUTHORIZATION, token); 
		response.setStatus(HttpStatus.OK.value()); // 자격 증명 성공 응답코드 리턴
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
					HttpServletResponse response, AuthenticationException failed)
					throws IOException, ServletException {

		// 콘솔에 메시지 출력
 		System.out.println("unsuccessfulAuthentication:" + failed); 

		// 자격 증명 실패 응답코드 리턴
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
