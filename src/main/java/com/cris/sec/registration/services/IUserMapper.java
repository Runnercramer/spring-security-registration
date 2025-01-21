package com.cris.sec.registration.services;

import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.entities.User;

public interface IUserMapper {

    UserDTO mapToUserDTO(User user);
    User mapToUser(UserDTO userDTO);
}
