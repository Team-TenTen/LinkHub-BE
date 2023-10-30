package com.tenten.linkhub.global.util.email;

import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class VerificationCodeCreator {
    private final SecureRandom secureRandom;

    public VerificationCodeCreator() throws NoSuchAlgorithmException {
        secureRandom = SecureRandom.getInstanceStrong();
    }

    public String createVerificationCode(){
        int randomNumber = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(randomNumber);
    }
}
