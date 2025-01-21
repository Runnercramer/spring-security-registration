package com.cris.sec.registration.services;

import com.cris.sec.registration.exceptions.UserAlreadyExistsException;
import com.cris.sec.registration.model.dtos.UserDTO;

public interface IUserService {
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;
}
