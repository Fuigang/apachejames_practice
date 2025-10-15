package com.kedu.file.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;



@Component
public class JwtUtil {
	@Value("${jwt.expiration}")
	private Long exp; // 만료 시간
	
	private Algorithm algorithm;  //무슨 암호화 할건지
	
	private JWTVerifier jwt; 
	
	
	
	public JwtUtil(@Value("${jwt.secret}") String secret) { //비밀번호 
		this.algorithm = Algorithm.HMAC256(secret);
		this.jwt = JWT.require(algorithm).build();
	}
		
	
	public String createToken (String id) {
		return JWT.create().withSubject(id).withClaim("name","tom")
		.withIssuedAt(new Date()).withExpiresAt(new Date(System.currentTimeMillis() + exp))
		.sign(this.algorithm); //토큰에 대표되는 어쩌구
		//토큰을 만들건데 우리가 데이터를 맘대로 넣을 수 있어요 진짜 아무거나 근데 의미가있는걸 넣어야겠지요
		//  withSubject 얘는 대표 데이터를 보통 넣습니다 id를 보통 넣어요
		// 다른것도 넣고싶으면 withClaim("name","tom") 를 써서 만들자
		//언제 발급 되는지를 알고싶으면  withIssuedAt(new Date() 사용하자
		// header / payload  / signature  =  JWT
		// 
		// 우리가 작성한 데이터들을 인코딩하지않고 payload에 들어간다
		// signature는 암호화게 되어있다.
		// 헤더와 페이로드를 연결해 시크릿값을 더해 HMAC256으로 돌려서 signature 가 된다
		//무결성 검증을 signature 가 한다
		
		
	}
	
	public DecodedJWT verifyToken(String token) {
		
		return jwt.verify(token);
	}

	
}
