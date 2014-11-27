package com.sigma.gclc.service;


import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sigma.gclc.objet.ImageCarrousel;

@Service
public class ServiceImage {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private File imageDirectory;
	
	@Value("${imagesDirectory}")
	public void setImagesDirectory(File directory) {
		this.imageDirectory = directory;
	}

	public List<ImageCarrousel> listerImagesRepertoire(){
		
		if (!imageDirectory.exists()) {
			logger.warn(" Repertoire '{}' non trouvé", imageDirectory);
			return new ArrayList<ImageCarrousel>(0);
		}
		
		//Paths.get(imageDirectory.toURI()).;
		
		File[] fichiers = imageDirectory.listFiles();
		int nombreImages = fichiers.length;
		logger.warn(" Nombre d'images trouvés : '{}' ", nombreImages);
		
		List<ImageCarrousel> images = new ArrayList<ImageCarrousel>(nombreImages);
		for (int i=0; i<nombreImages; i++ ) {
			ImageCarrousel image = new ImageCarrousel(MessageFormat.format("images/{0}", fichiers[i].getName()));
			images.add(image);
		} 
	
		return images;
	}
}
