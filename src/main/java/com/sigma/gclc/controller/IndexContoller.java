package com.sigma.gclc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexContoller {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
