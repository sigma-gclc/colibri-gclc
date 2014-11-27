package com.sigma.gclc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigma.gclc.objet.ImageCarrousel;

@RestController
public class CarrouselController {


		private List<ImageCarrousel> images = new ArrayList<ImageCarrousel>(50);

		
		public List<ImageCarrousel> getImages() {
			return images;
		}

		public void setImages(List<ImageCarrousel> images) {
			this.images = images;
		}

	
	

	@RequestMapping(value = "/imagesCarrousel", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ImageCarrousel> listerImagesCarrousel() {
		getImages().add(new ImageCarrousel("Premiere image", 0));
		getImages().add(new ImageCarrousel("Deuxieme image", 2));
		return getImages();
	}
	
	
	
}
