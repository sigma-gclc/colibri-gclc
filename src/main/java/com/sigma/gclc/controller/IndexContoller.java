package com.sigma.gclc.controller;

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
    public String index(Model model) {
    	model.addAttribute("imagesListe", serviceImage.listerImagesRepertoire());
    	return "index";
    }
}
