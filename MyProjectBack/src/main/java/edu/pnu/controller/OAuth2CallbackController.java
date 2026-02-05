package edu.pnu.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.config.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuth2CallbackController {
//	private final StringRedisTemplate redisTemplate;
	@PostMapping("/api/jwtcallback")
	public ResponseEntity<?> apiCallback(@RequestBody Map<String, String> body) {
		String code = body.get("code");
		System.out.println("[OAuth2CallbackController]code:" + code);
		// 1. 코드 검증
		String token = OAuth2SuccessHandler.tempCodeStore.get(code);
//		String token = redisTemplate.opsForValue().get(code);
		System.out.println("[OAuth2CallbackController]token:" + token);
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 코드입니다.");
		}
		// 2. 사용된 코드는 즉시 삭제 (일회용)
		OAuth2SuccessHandler.tempCodeStore.remove(code);
//		redisTemplate.delete(code);
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
	}
}
