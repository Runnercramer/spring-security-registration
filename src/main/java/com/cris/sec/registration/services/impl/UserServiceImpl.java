package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.exceptions.UserAlreadyExistsException;
import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.repositories.UserRepository;
import com.cris.sec.registration.services.IUserMapper;
import com.cris.sec.registration.services.IUserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final IUserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, IUserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    @Override
    public void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException {

        if(this.accountExists(userDTO.getEmail())){
            throw new UserAlreadyExistsException("Ya existe un usuario registrado con este email: " + userDTO.getEmail());
        }

        this.userRepository.save(this.userMapper.mapToUser(userDTO));

    }

    private boolean accountExists(String email){
        return this.userRepository.findByEmail(email) != null;
    }
}
