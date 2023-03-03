package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.user.request.EmailCheckRequest;
import com.paintourcolor.odle.dto.user.request.EmailCodeRequest;

public interface EmailServiceInterface {
    void sendEmail(EmailCheckRequest emailCheckRequest)throws Exception;

    void verifyCode(EmailCodeRequest emailCodeRequest);
}
