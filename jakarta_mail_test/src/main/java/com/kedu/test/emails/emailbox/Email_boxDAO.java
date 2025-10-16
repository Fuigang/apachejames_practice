package com.kedu.test.emails.emailbox;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
	public class Email_boxDAO {

	    @Autowired
	    private SqlSession mybatis;

	    private static final String NAMESPACE = "EmailboxMapper.";

	    public Integer findEmailboxSeqByEmailAndType(String memberEmail, String emailboxType) {
	        // 파라미터를 2개 이상 넘기기 위해 Map을 사용합니다.
	        Map<String, String> params = Map.of(
	            "member_email", memberEmail,
	            "emailbox_type", emailboxType
	        );
	        return mybatis.selectOne(NAMESPACE + "findEmailboxSeqByEmailAndType", params);
	    }
	 // Email_boxDAO.java 클래스 안에 이 메소드를 추가합니다.
	    public void createEmailbox(Email_boxDTO dto) {
	        // 이 메소드가 실행되면, 파라미터로 받은 dto 객체의 emailbox_seq 필드에
	        // 방금 생성된 시퀀스 값이 채워집니다.
	        mybatis.insert(NAMESPACE + "createEmailbox", dto);
	    }
	    
}
