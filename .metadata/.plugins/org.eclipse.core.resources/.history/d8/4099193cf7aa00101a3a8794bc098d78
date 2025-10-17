package com.kedu.test.emails.email;

import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class Email_accountService {

    private final String DOCKER_CONTAINER_NAME = "my-james-server";

    public void createMailAccount(String username, String password) throws Exception {
        String[] command = { "docker", "exec", "-it", DOCKER_CONTAINER_NAME, "james-cli", "adduser", username, password };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            // 비즈니스 로직 상의 실패: Docker 명령어는 성공했지만, James가 사용자를 추가하지 못함
            if (exitCode != 0) {
                // '비상벨'을 울린다!
                throw new Exception("메일 서버(James)에 계정을 생성하는 데 실패했습니다.");
            }
            
            // 성공 시에는 조용히 종료
            System.out.println("James 서버에 계정 '" + username + "' 생성을 성공적으로 요청했습니다.");

        } catch (IOException | InterruptedException e) {
            // 시스템 레벨의 실패: Docker 명령 자체를 실행하지 못하는 등 더 심각한 오류
            // '위험물' 사고가 발생하면, 즉시 '비상벨'을 울린다!
            throw new Exception("메일 계정 생성 프로세스 실행 중 시스템 오류가 발생했습니다.", e);
        }
    }
}