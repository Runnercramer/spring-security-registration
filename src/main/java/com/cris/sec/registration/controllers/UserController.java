package com.cris.sec.registration.controllers;

import com.cris.sec.registration.exceptions.UserAlreadyExistsException;
import com.cris.sec.registration.model.dtos.UserDTO;
import com.cris.sec.registration.services.IUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService){this.userService = userService;}

    @GetMapping(path = "/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @PostMapping(path = "/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO,
                                            BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        if (result.hasErrors()){
            modelAndView.addObject("errors", result.getAllErrors());
            return modelAndView;
        }
        try {
            this.userService.registerNewUser(userDTO);
        } catch (UserAlreadyExistsException e) {
            modelAndView.addObject("message", "Ya existe una cuenta registrada para este email: " + userDTO.getEmail());
            return modelAndView;
        }
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
