package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.EmailCheckRequest;
import com.paintourcolor.odle.dto.user.request.EmailCodeRequest;
import com.paintourcolor.odle.entity.EmailCode;
import com.paintourcolor.odle.entity.EmailVerifyEnum;
import com.paintourcolor.odle.repository.EmailCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailServiceInterface{
    private final EmailCodeRepository emailCodeRepository;
    private final JavaMailSender emailSender;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    // 메일 생성
    private MimeMessage createMessage(String userEmail, String ePw)throws Exception{
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, userEmail);// 요청한 유저
        message.setSubject("Odle 회원가입 이메일 인증");// 메일 제목

        String msgg="";
        msgg+= "<img src=https://odle.s3.ap-northeast-2.amazonaws.com/profile/e0a6feb0-3ff1-4511-8ee9-1fc1a8b8f897-Odle.png>";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<br>";
        msgg+= "<p>이 메일은 발신 전용입니다.<p>";
        msgg+= "<br>";
        msgg+= "<h1> 안녕하세요 Odle 입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다!<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html"); // 메일 안에 내용
        message.setFrom(new InternetAddress("odle7@naver.com","Odle")); // 메일 보내는 사람

        return message;
    }

    // 인증 코드 생성
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 무작위

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // 알파벳 소문자
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // 알파벳 대문자
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 숫자
                    break;
            }
        }

        return key.toString();
    }

    // 인증 이메일 발송
    @Transactional
    @Override
    public void sendEmail(EmailCheckRequest emailCheckRequest) throws Exception {
        String userEmail = emailCheckRequest.getEmail();
        String ePw = createKey();
        MimeMessage message = createMessage(userEmail, ePw);
        EmailCode emailCode = emailCodeRepository.findByEmail(userEmail);
        EmailVerifyEnum verifyEnum = EmailVerifyEnum.UNVERIFIED;
        try{
            emailSender.send(message); // 메일 보내기
            // 인증 코드 DB에 저장
            if (emailCodeRepository.existsByEmail(userEmail)) { // 기존에 보내진 인증 코드가 있을 경우 -> 인증 코드만 업데이트
                emailCode.updateCode(ePw);
            } else { // 저장된 인증 코드가 없어서 새로 생성
                emailCodeRepository.save(new EmailCode(userEmail, ePw, verifyEnum));
            }

        }catch(MailException es){
            es.printStackTrace(); // 에러 발생의 근원지를 찾아서 단계별로 에러를 출력
            throw new RuntimeException("서버에서 정상적으로 처리되지 않았습니다.");
        }

        // 인증코드 메일 발송 1시간 후 삭제
        executorService.schedule(() -> {
            // 저장된 row를 삭제
            LocalDateTime now = LocalDateTime.now();
            EmailCode expiredCode = emailCodeRepository.findByCreatedTimeBefore(now.minusHours(1));
            emailCodeRepository.delete(expiredCode);
        }, 1, TimeUnit.HOURS);
    }

    // 인증 코드 확인
    @Transactional
    @Override
    public void verifyCode(EmailCodeRequest emailCodeRequest) {
        String email = emailCodeRequest.getEmail();
        String code = emailCodeRequest.getCode();
        EmailCode emailCode = emailCodeRepository.findByEmail(email);
        // 만약 DB에 저장돼있는 이메일에 해당하는 코드랑 리퀘스트로 받아온 코드가 일치하지 않는다면 실패
        if (!emailCodeRepository.findByEmail(email).getCode().equals(code)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
        emailCode.updateVerify(EmailVerifyEnum.VERIFIED);
    }
}

