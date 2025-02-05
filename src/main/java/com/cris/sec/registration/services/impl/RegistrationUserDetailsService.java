package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.model.entities.User;
import com.cris.sec.registration.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RegistrationUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationUserDetailsService.class);

    private final UserRepository userRepository;

    public RegistrationUserDetailsService (UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOG.info("[RegistrationUserDetailsService] Start loadUserByUserName");
        User user = this.userRepository.findByEmail(email);
        LOG.debug("User found: {}", user);
        if (user == null) throw new UsernameNotFoundException("No se encontró un usuario con este email: " + email);

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword().toLowerCase(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(user.getRoles()));
    }

    private static List<GrantedAuthority> getAuthorities(List<String> roles) {
        LOG.info("[RegistrationUserDetailsService] Start getAuthorities");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        LOG.debug("Authorities: {}", authorities);
        return authorities;
    }
}
