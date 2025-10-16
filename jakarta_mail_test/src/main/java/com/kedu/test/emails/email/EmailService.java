package com.kedu.test.emails.email;

import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kedu.test.emails.emailbox.Email_boxService;
import com.kedu.test.emails.emailsender.Email_senderDAO;
import com.kedu.test.emails.emailsender.Email_senderDTO;
import com.kedu.test.members.MemberDTO;
import com.kedu.test.members.MemberService;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    // --- 의존성 주입: '총괄 매니저'가 지시를 내릴 모든 '실무 담당자'들 ---
    private final EmailDAO emailDAO;
    private final Email_senderDAO email_senderDAO;
    private final MemberService memberService;
    private final Email_boxService email_boxService;

    // 생성자를 통해 모든 담당자를 한 번에 배정받습니다.
    public EmailService(EmailDAO emailDAO, Email_senderDAO email_senderDAO, MemberService memberService, Email_boxService email_boxService) {
        this.emailDAO = emailDAO;
        this.email_senderDAO = email_senderDAO;
        this.memberService = memberService;
        this.email_boxService = email_boxService;
    }

    // --- DAO 래퍼 메소드 (팀장님 요청 스타일) ---

    public void saveEmail(EmailDTO emailDto) {
        emailDAO.saveEmail(emailDto);
    }

    public void saveSender(Email_senderDTO emailSenderDto) {
        email_senderDAO.saveSender(emailSenderDto);
    }

    // --- 실제 비즈니스 로직 ---

    /**
     * 고정된 테스트 데이터로 메일 발송을 시뮬레이션하는 메소드.
     * @throws MessagingException 메일 발송 실패 시
     */
    @Transactional
    public void sendTestMail() throws MessagingException {
        // --- 1. 테스트 데이터 정의 ---
        String senderEmail = "user01@test.com";
        String recipientEmail = "user01@test.com"; // 자기 자신에게 발송

        // --- 2. 다른 서비스를 호출하여 필요한 정보 조회 ---
        // '보낸편지함'이 없으면 자동으로 생성하고, 그 ID를 가져옵니다.
        int sentBoxSeq = email_boxService.findOrCreateEmailboxSeq(senderEmail, "보낸편지함");
        // DB에서 발송자의 비밀번호를 가져옵니다.
        MemberDTO sender = memberService.findByEmail(senderEmail);
        final String password = sender.getPw();

        // --- 3. 우리 DB에 '보낸 메일' 기록 (래퍼 메소드 사용) ---
        EmailDTO emailDto = new EmailDTO();
        emailDto.setEmailbox_seq(sentBoxSeq);
        emailDto.setTitle("스프링 부트에서 보낸 테스트 메일");
        emailDto.setEmail_from(senderEmail);
        emailDto.setContent("이 메일이 성공적으로 보내졌다면, 발송 기능 구현이 완료된 것입니다.");
        this.saveEmail(emailDto); // emailDto의 email_seq가 채워짐

        Email_senderDTO senderDto = new Email_senderDTO();
        senderDto.setEmail_seq(emailDto.getEmail_seq());
        senderDto.setSender_email(recipientEmail);
        this.saveSender(senderDto);

        // --- 4. James 메일 서버에 실제 발송 요청 (동적 로그인) ---
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // DB에서 조회한 실제 사용자의 ID와 PW를 사용합니다.
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(emailDto.getTitle());
        message.setText(emailDto.getContent());

        Transport.send(message);
    }

    /**
     * 특정 사용자의 보낸 편지함 목록을 조회하는 메소드.
     * @param userEmail 조회할 사용자의 이메일
     * @return List<EmailDTO> 보낸 메일 목록
     */
    public List<EmailDTO> getSentMails(String userEmail) {
        // 1. 사용자의 '보낸편지함' ID를 찾습니다.
        int sentBoxSeq = email_boxService.findOrCreateEmailboxSeq(userEmail, "보낸편지함");
        
        // 2. 해당 ID를 가진 모든 메일을 DB에서 조회하여 반환합니다.
        return emailDAO.findByEmailboxSeq(sentBoxSeq);
    }
}