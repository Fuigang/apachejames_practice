package com.kedu.test.members;

import com.kedu.test.common.Encryptor;
import com.kedu.test.emails.email.Email_accountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDAO dao;
    private final Email_accountService email_accountService; // 패키지명 소문자로 수정 제안

    // --- 생성자를 통해 모든 의존성을 한 번에 주입받는 것이 표준 방식입니다 ---
    public MemberService(MemberDAO dao, Email_accountService email_accountService) {
        this.dao = dao;
        this.email_accountService = email_accountService;
    }

    /**
     * 이메일로 사원 정보를 조회하는 메소드 (★★ 바로 이 메소드가 빠져있었습니다! ★★)
     * @param email 조회할 이메일
     * @return MemberDTO 조회된 사원 정보
     */
    public MemberDTO findByEmail(String email) {
        // DAO를 호출하여 결과를 그대로 반환합니다.
        return dao.findByEmail(email);
    }
		
    /**
     * 회원가입과 동시에 James 메일 서버에 계정을 생성합니다.
     */
    @Transactional
    public int signup(MemberDTO dto) throws Exception {
        // James 계정 생성
        email_accountService.createMailAccount(dto.getEmail(), dto.getPw());
        
        // 우리 DB 저장용 비밀번호 암호화
        dto.setPw(Encryptor.encrypt(dto.getPw())); 
        return dao.signup(dto);
    }
	
	// ... (로그인, 비밀번호 찾기 등 다른 메소드들) ...
}