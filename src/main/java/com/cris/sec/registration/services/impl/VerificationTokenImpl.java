package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.exceptions.TokenNotFoundException;
import com.cris.sec.registration.model.dtos.VerificationTokenDTO;
import com.cris.sec.registration.model.entities.VerificationToken;
import com.cris.sec.registration.model.repositories.VerificationTokenRepository;
import com.cris.sec.registration.services.IUserMapper;
import com.cris.sec.registration.services.IVerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenImpl implements IVerificationTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(VerificationTokenImpl.class);

    private final VerificationTokenRepository verificationTokenRepository;
    private final IUserMapper userMapper;

    public VerificationTokenImpl (VerificationTokenRepository verificationTokenRepository, IUserMapper userMapper){
        this.verificationTokenRepository = verificationTokenRepository;
        this.userMapper = userMapper;
    }

    @Override
    public VerificationTokenDTO findVerificationToken(String token) throws TokenNotFoundException {
        LOG.info("[VerificationTokenImpl] Start findVerificationToken");
        VerificationToken verificationToken = this.verificationTokenRepository.findByToken(token);
        if (verificationToken == null){
            LOG.error("Throwing new TokenNoFoundException");
            throw new TokenNotFoundException("El token ingresado no existe");
        }
        VerificationTokenDTO response = new VerificationTokenDTO();
        response.setToken(verificationToken.getToken());
        response.setUser(this.userMapper.mapToUserDTO(verificationToken.getUser()));
        response.setExpirationDate(verificationToken.getExpirationDate());
        LOG.debug("VerificationTokenDTO: {}", verificationToken);
        return response;
    }

    @Override
    public void persistVerificationToken(VerificationTokenDTO verificationTokenDTO) {
        LOG.info("[VerificationTokenImpl] Start persistVerificationToken");
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(verificationTokenDTO.getToken());
        verificationToken.setUser(userMapper.mapToUser(verificationTokenDTO.getUser()));
        LOG.debug("VerificationTokenDTO: {}", verificationTokenDTO);
        this.verificationTokenRepository.save(verificationToken);
    }
}
