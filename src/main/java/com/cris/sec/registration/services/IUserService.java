package com.cris.sec.registration.services;

import com.cris.sec.registration.exceptions.TokenNotFoundException;
import com.cris.sec.registration.exceptions.UserAlreadyExistsException;
import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.dtos.VerificationTokenDTO;

public interface IUserService {
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;

    void createVerificationToken(UserDTO userDTO, String token);

    void saveRegisteredUser(UserDTO userDTO);

    UserDTO getUser(String verificationToken) throws TokenNotFoundException;

    VerificationTokenDTO getVerificationToken(String VerificationToken) throws TokenNotFoundException;
}
