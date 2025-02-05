package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.entities.User;
import com.cris.sec.registration.services.IUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapperImpl implements IUserMapper {

    private static final Logger LOG = LoggerFactory.getLogger(UserMapperImpl.class);

    private final ApplicationContext applicationContext;

    public UserMapperImpl(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    @Override
    public UserDTO mapToUserDTO(User user) {
        LOG.debug("[UserMapperImpl] Start mapToUserDTO");
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
        LOG.debug("[UserMapperImpl] Start mapToUser");
        User user = new User();
        user.setNames(userDTO.getNames());
        user.setLastNames(userDTO.getLastNames());
        user.setEmail(userDTO.getEmail());
        user.setPassword(this.applicationContext.getBean(BCryptPasswordEncoder.class).encode(userDTO.getPassword()));
        user.setMatchingPassword(this.applicationContext.getBean(BCryptPasswordEncoder.class).encode(userDTO.getMatchingPassword()));
        user.setRoles(List.of("USER"));
        user.setEnabled(userDTO.isEnabled());
        return user;
    }
}
