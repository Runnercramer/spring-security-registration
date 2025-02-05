package com.cris.sec.registration.controllers;

import com.cris.sec.registration.exceptions.TokenNotFoundException;
import com.cris.sec.registration.exceptions.UserAlreadyExistsException;
import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.model.dtos.VerificationTokenDTO;
import com.cris.sec.registration.model.events.OnRegistrationCompleteEvent;
import com.cris.sec.registration.services.IUserService;
import com.cris.sec.registration.services.IVerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Locale;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String MESSAGE_TEXT = "message";
    private static final String RETURNING_TEXT = "Returning: {}";

    private final IUserService userService;
    private final IVerificationTokenService verificationTokenService;
    private final ApplicationContext applicationContext;
    private final MessageSource messageSource;

    public UserController(IUserService userService, ApplicationContext applicationContext, IVerificationTokenService  verificationTokenService, MessageSource messageSource){
        this.userService = userService;
        this.applicationContext = applicationContext;
        this.verificationTokenService = verificationTokenService;
        this.messageSource = messageSource;
    }

    @GetMapping(path = "/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @PostMapping(path = "/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO,
                                            BindingResult result,
                                            HttpServletRequest request) {
        LOG.info("[UserController] Start registerUserAccount: {}", userDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        if (result.hasErrors()){
            LOG.warn("BindingResult has {} errors", result.getErrorCount());
            modelAndView.addObject("errors", result.getAllErrors());
            LOG.debug(RETURNING_TEXT, modelAndView);
            return modelAndView;
        }
        try {
            this.userService.registerNewUser(userDTO);
            OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(userDTO, request.getLocale(), request.getContextPath());
            LOG.info("Publishing a new OnRegistrationCompleteEvent: {}", event);
            this.applicationContext.publishEvent(event);
        } catch (UserAlreadyExistsException e) {
            LOG.error("UserAlreadyExistsException caught: {}", e.getMessage());
            modelAndView.addObject(MESSAGE_TEXT, "Ya existe una cuenta registrada para este email: " + userDTO.getEmail());
            LOG.debug(RETURNING_TEXT, modelAndView);
            return modelAndView;
        } catch (RuntimeException ex) {
            LOG.error("RuntimeException caught: {}", ex.getMessage());
            modelAndView.setViewName("emailError");
            modelAndView.addObject("user", userDTO);
            LOG.debug(RETURNING_TEXT, modelAndView);
            return modelAndView;
        }
        modelAndView.setViewName("index");
        LOG.info(RETURNING_TEXT, modelAndView);
        return modelAndView;
    }

    @GetMapping("/registrationConfirm")
    public String registrationConfirm(WebRequest webRequest,
                                      Model model,
                                      @RequestParam("token") String token){
        LOG.info("[UserController] Start registrationConfirm");
        Locale locale = webRequest.getLocale();
        try {
            VerificationTokenDTO verificationToken = this.verificationTokenService.findVerificationToken(token);
            UserDTO userDTO = verificationToken.getUser();
            LOG.debug("User: {}", userDTO);

            Calendar calendar = Calendar.getInstance();
            if(verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime() <= 0){
                LOG.debug("Token is expired");
                String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
                model.addAttribute(MESSAGE_TEXT, messageValue);
                return "redirect:/badUser.html?lang=" + locale.getLanguage();
            }

            userDTO.setEnabled(true);
            userService.saveRegisteredUser(userDTO);
            return "redirect:/login.html?lang=" + webRequest.getLocale().getLanguage();
        } catch (TokenNotFoundException e) {
            LOG.warn("TokenNotFoundException: {}", e.getMessage());
            String message = this.messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute(MESSAGE_TEXT, message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }
    }

    @GetMapping("/login")
    public ModelAndView login(){
        LOG.info("[UserController] Start login");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
    }
}
