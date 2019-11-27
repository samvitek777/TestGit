package com.example.sweater.controller;

import com.example.sweater.domain.User;
import com.example.sweater.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.Map;

import static com.example.sweater.controller.ControllerUtils.getErrors;


@Controller
public class RegistrationController {
    @Autowired
    private UserSevice userSevice;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model){

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        if(isConfirmEmpty){
            model.addAttribute("password2Error", "Password confirmator");
        }

        if(user.getPassword() != null && !user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordError", "Password are not different!");
        }

        if(isConfirmEmpty || bindingResult.hasErrors()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.addAttribute(errors);

            return "registration";
        }
        if(!userSevice.addUser(user)){
            model.addAttribute("usernameError", "User, exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivatrd = userSevice.activateUser(code);
        if(isActivatrd){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
