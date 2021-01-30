package com.veegee.polls.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String swaggerRedirect() {
        return "redirect:/swagger-ui.html";
    }
}
