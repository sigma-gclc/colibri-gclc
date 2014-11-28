package com.sigma.gclc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigma.gclc.objet.ImageCarrousel;

@Service
public class ServiceImage {

	@Autowired
	private ImageRepository imageRepository;
	

	public List<ImageCarrousel> listerImagesRepertoire() {
		return imageRepository.getImages();
		

	}
}
