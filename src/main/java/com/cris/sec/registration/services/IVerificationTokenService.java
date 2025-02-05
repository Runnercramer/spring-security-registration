package com.cris.sec.registration.services;

import com.cris.sec.registration.exceptions.TokenNotFoundException;
import com.cris.sec.registration.model.dtos.VerificationTokenDTO;

public interface IVerificationTokenService {

    VerificationTokenDTO findVerificationToken(String token) throws TokenNotFoundException;

    void persistVerificationToken(VerificationTokenDTO verificationTokenDTO);
}
