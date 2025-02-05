package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.exceptions.TokenNotFoundException;
import com.cris.sec.registration.exceptions.UserAlreadyExistsException;
import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.dtos.VerificationTokenDTO;
import com.cris.sec.registration.model.repositories.UserRepository;
import com.cris.sec.registration.services.IUserMapper;
import com.cris.sec.registration.services.IUserService;
import com.cris.sec.registration.services.IVerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final IUserMapper userMapper;
    private final IVerificationTokenService verificationTokenService;

    public UserServiceImpl(UserRepository userRepository, IUserMapper userMapper, IVerificationTokenService verificationTokenService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.verificationTokenService = verificationTokenService;
    }
    @Override
    public void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException {
        LOG.info("[UserServiceImpl] Start registerNewUser");
        if(this.accountExists(userDTO.getEmail())){
            LOG.error("Throwing new UserAlreadyExistsException");
            throw new UserAlreadyExistsException("Ya existe un usuario registrado con este email: " + userDTO.getEmail());
        }
        LOG.debug("Saving new user: {}", userDTO);
        this.userRepository.save(this.userMapper.mapToUser(userDTO));

    }

    @Override
    public void createVerificationToken(UserDTO userDTO, String token) {
        LOG.info("[UserServiceImpl] Start createVerificationToken");
        if (this.accountExists(userDTO.getEmail())){
            VerificationTokenDTO verificationToken = new VerificationTokenDTO();
            verificationToken.setToken(token);
            verificationToken.setUser(userDTO);
            LOG.debug("Saving new verification token for user: {}", userDTO);
            this.verificationTokenService.persistVerificationToken(verificationToken);
        }
    }

    @Override
    public void saveRegisteredUser(UserDTO userDTO) {
        LOG.info("[UserServiceImpl] Start saveRegisteredUser");
        this.userRepository.save(this.userMapper.mapToUser(userDTO));
    }

    @Override
    public UserDTO getUser(String verificationToken) throws TokenNotFoundException {
        LOG.info("[UserServiceImpl] Start getUser");
        UserDTO user;
        try {
            user = this.verificationTokenService.findVerificationToken(verificationToken).getUser();
            LOG.debug("User found: {}", user);
        } catch (TokenNotFoundException e) {
            LOG.error("Throwing new TokenNotFoundException");
            throw new TokenNotFoundException(e.getMessage());
        }
        return user;
    }

    @Override
    public VerificationTokenDTO getVerificationToken(String verificationToken) throws TokenNotFoundException {
        LOG.info("[UserServiceImpl] Start getVerificationToken");
        try {
            return this.verificationTokenService.findVerificationToken(verificationToken);
        } catch (TokenNotFoundException e) {
            LOG.error("Throwing new TokenNotFoundException");
            throw new TokenNotFoundException(e.getMessage());
        }
    }

    private boolean accountExists(String email){
        LOG.info("[UserServiceImpl] Start accountExists");
        return this.userRepository.findByEmail(email) != null;
    }
}
