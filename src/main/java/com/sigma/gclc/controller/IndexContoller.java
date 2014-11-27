package com.sigma.gclc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sigma.gclc.service.ServiceImage;

@Controller
public class IndexContoller {

	@Autowired
	private ServiceImage serviceImage;
	
    @RequestMapping(value = "/")
    public String index(Model model) throws IOException {
    	model.addAttribute("imagesListe", serviceImage.listerImagesRepertoire());
    	return "index";
    }
}
