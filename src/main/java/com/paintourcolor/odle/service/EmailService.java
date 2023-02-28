package com.paintourcolor.odle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailServiceInterface{
    private final JavaMailSender emailSender;
    public static final String ePw = createKey();

    // 메일 생성
    private MimeMessage createMessage(String to)throws Exception{
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 요청한 유저
        message.setSubject("Odle 회원가입 이메일 인증");// 메일 제목

        String msgg="";
        msgg+= "<img src=https://odle.s3.ap-northeast-2.amazonaws.com/profile/e0a6feb0-3ff1-4511-8ee9-1fc1a8b8f897-Odle.png>";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<br>";
        msgg+= "<p>이 메일은 발신 전용입니다.<p>";
        msgg+= "<br>";
        msgg+= "<h1> 안녕하세요 Odel 입니다. </h1>";
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
    public void sendEmail(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try{
            emailSender.send(message);
        }catch(MailException es){
//            es.printStackTrace(); // 에러 발생의 근원지를 찾아서 단계별로 에러를 출력
            es.getMessage(); // 에러의 원인을 간단하게 출력
            throw new RuntimeException("서버에서 정상적으로 처리되지 않았습니다.");
        }
    }
}

