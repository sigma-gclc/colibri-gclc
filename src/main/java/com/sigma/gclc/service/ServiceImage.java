package com.sigma.gclc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sigma.gclc.objet.ImageCarrousel;

@Service
public class ServiceImage {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private File imageDirectory;

	@Value("${imageMaxLength:1000000}")
	private long maxLength;

	@Value("${imagesDirectory}")
	public void setImagesDirectory(File directory) {
		this.imageDirectory = directory;
	}

	public List<ImageCarrousel> listerImagesRepertoire() throws IOException {

		if (!imageDirectory.exists()) {
			logger.warn(" Repertoire '{}' non trouvé", imageDirectory);
			return new ArrayList<ImageCarrousel>(0);
		}

		File[] fichiers = imageDirectory.listFiles();
		int nombreImages = fichiers.length;
		logger.info(" Nombre d'images trouvés : '{}' ", nombreImages);

		List<ImageCarrousel> images = new ArrayList<ImageCarrousel>(
				nombreImages);
		for (int i = 0; i < nombreImages; i++) {
			File aFile = fichiers[i];
			if (!"prop".equals(FilenameUtils.getExtension(aFile.getName()))) {
				// c'est une image !
				if (aFile.length() > maxLength) {
					continue; // image non conservée => too big
				}
				ImageCarrousel image = new ImageCarrousel(MessageFormat.format(
						"images/{0}", fichiers[i].getName()));
				// pour la description => load le fichier property correspondant
				File descriptionFile = new File(imageDirectory,
						FilenameUtils.getBaseName(aFile.getName()) + ".prop");
				if (descriptionFile.exists()) {
					Properties props = new Properties();
					props.load(new FileInputStream(descriptionFile));
					image.setDescription(props.getProperty("Description"));
				}
				images.add(image);
			}

		}

		return images;
	}
}
