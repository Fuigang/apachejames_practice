package com.kedu.file.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kedu.file.dto.AuthDTO;
import com.kedu.file.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	
	@Autowired
	private JwtUtil jwt;
	
	
	@PostMapping
	public ResponseEntity<String> login(@RequestBody AuthDTO dto){
		
		if(true) { // 로그인에 성공했을 경우
			//토큰 생성 자리 
			String token = jwt.createToken(dto.getId());
			return ResponseEntity.ok(token);			
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Incorrect");
		
		
	}
	@GetMapping("/test")
	public ResponseEntity<String> test(HttpServletRequest request){
		
		try {
			String authHeader = request.getHeader("Authorization");
			String token = authHeader.substring(7);
			DecodedJWT djwt = jwt.verifyToken(token);
			System.out.println(djwt.getSubject());
		
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("장난 검지검지");
	}
		return ResponseEntity.ok("인증자 전용 데이터");}
	
}
