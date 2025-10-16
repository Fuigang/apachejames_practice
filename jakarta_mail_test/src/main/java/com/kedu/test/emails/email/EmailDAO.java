package com.kedu.test.emails.email;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kedu.test.emails.emailsender.Email_senderDTO;

@Repository
public class EmailDAO {

	@Autowired
    private SqlSession mybatis;
	
	private static final String NAMESPACE = "EmailMapper.";
	
	public void saveEmail(EmailDTO emailDTO) {
        mybatis.insert(NAMESPACE + "saveEmail", emailDTO);
    }

    /**
     * 보낸 메일의 수신자 정보를 email_sender 테이블에 저장합니다.
     * @param emailSenderDto 저장할 수신자 정보
     */
    public void saveSender(Email_senderDTO email_senderDTO) {
        mybatis.insert(NAMESPACE + "saveSender", email_senderDTO);
    }
    
 // EmailDAO.java 클래스에 이 메소드를 추가합니다
    public List<EmailDTO> findByEmailboxSeq(int emailboxSeq) {
        return mybatis.selectList(NAMESPACE + "findByEmailboxSeq", emailboxSeq);
    }
	
}
