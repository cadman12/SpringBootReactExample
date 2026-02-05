package edu.pnu.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

public class JWTUtil {
	private static final long ACCESS_TOKEN_MSEC = 24*60*(60*1000); // 1일
	private static final String JWT_KEY = "edu.pnu.jwtkey";

	public static final String prefix = "Bearer ";
	public static final String usernameClaim = "username"; 

	private static String getJWTSource(String token) {
		if (token.startsWith(prefix)) return token.replace(prefix, "");
		return token;
	}

	// username을 이용한 JWT 생성 메서드
	public static String getJWT(String username) {
		String src = JWT.create()
									.withClaim(usernameClaim, username)
									.withExpiresAt(new Date(System.currentTimeMillis()
																						+ACCESS_TOKEN_MSEC))
									.sign(Algorithm.HMAC256(JWT_KEY));
		return prefix + src;
	}
	// JWT에서 cname 값을 추출
	public static String getClaim(String token, String cname) {
		String tok = getJWTSource(token);
		Claim claim = JWT.require(Algorithm.HMAC256(JWT_KEY)).build()
									.verify(tok).getClaim(cname);
		if (claim.isMissing()) return null;
		return claim.asString();
	}

	// JWT 토큰이 유효한지 검사
	public static boolean isExpired(String token) {
		String tok = getJWTSource(token);
		return JWT.require(Algorithm.HMAC256(JWT_KEY)).build()
						.verify(tok).getExpiresAt().before(new Date());
	}
}
