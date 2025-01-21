package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.entities.User;
import com.cris.sec.registration.services.IUserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements IUserMapper {
    @Override
    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setNames(user.getNames());
        userDTO.setLastNames(user.getLastNames());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setMatchingPassword(user.getMatchingPassword());
        return userDTO;
    }

    @Override
    public User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setNames(userDTO.getNames());
        user.setLastNames(userDTO.getLastNames());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setMatchingPassword(userDTO.getMatchingPassword());
        return user;
    }
}
