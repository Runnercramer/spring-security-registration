package com.cris.sec.registration.model.events;

import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationListener.class);
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final IUserService userService;
    @Value("${spring.mail.username}")
    private String sender;

    public RegistrationListener(JavaMailSender javaMailSender, IUserService userService,MessageSource messageSource){
        this.javaMailSender = javaMailSender;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        LOG.info("[RegistrationListener] Start onApplicationEvent");
        LOG.debug("Event: {}", event);
        UserDTO userDTO = event.getUser();
        String token = UUID.randomUUID().toString();
        this.userService.createVerificationToken(userDTO, token);

        String recipient = userDTO.getEmail();
        String subject = "Confirmación de registro";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messageSource.getMessage("message.regSucc", new Object[]{confirmationUrl}, event.getLocale());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        LOG.debug("Simple Mail Message: {}", simpleMailMessage);

        javaMailSender.send(simpleMailMessage);
    }
}
