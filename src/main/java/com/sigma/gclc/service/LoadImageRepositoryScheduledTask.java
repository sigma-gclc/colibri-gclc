package com.sigma.gclc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sigma.gclc.objet.ImageCarrousel;

@Component
public class LoadImageRepositoryScheduledTask {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ImageRepository imageRepository;

	private File imageDirectory;
	@Value("${imageMaxSize:5004000}")
	private long imageMaxSize;

	@Autowired
	public LoadImageRepositoryScheduledTask(ImageRepository imageRepository, @Value("${imagesDirectory}")File imageDirectory) {
		this.imageRepository = imageRepository;
		this.imageDirectory = imageDirectory;
	}

	/**
	 * Loade les images.
	 */
	@Scheduled(fixedRateString="${loadImageRepositoryTaskFixedRate:60000}", initialDelay= 0)
	public void refreshImageRepository() {
		if (!imageDirectory.exists()) {
			logger.warn(" Repertoire '{}' non trouvé", imageDirectory);
			imageRepository.reset();
			return; // the end
		}
		
		this.imageRepository.reset();

		// initialisation
		File[] fichiers = imageDirectory.listFiles();
		int nombreImages = fichiers.length;
		logger.info(" Nombre d'images trouvés : '{}' ", nombreImages);

		Properties props = null;
		File descriptionFile = null;
		for (int i = 0; i < nombreImages; i++) {
			File aFile = fichiers[i];
			if (!"prop".equals(FilenameUtils.getExtension(aFile.getName()))) {
				// c'est une image !
				if (aFile.length() > imageMaxSize) {
					logger.info("Image {} de taill {} (en octets) non conservée", aFile, aFile.length());
					continue; // image non conservée => too big
				}
				ImageCarrousel image = new ImageCarrousel(MessageFormat.format(
						"images/{0}", fichiers[i].getName()));
				// pour la description => load le fichier property correspondant
				descriptionFile = new File(imageDirectory,
						FilenameUtils.getBaseName(aFile.getName()) + ".prop");
				if (descriptionFile.exists()) {
					props = new Properties();
					try {
						props.load(new FileInputStream(descriptionFile));
						image.setDescription(props.getProperty("Description"));
					} catch (IOException ex) {
						logger.error("Erreur lecture {}: {}", descriptionFile,
								ex.getMessage());
					}
					
				} else {
					image.setDescription(aFile.getName());
				}
				imageRepository.addImage(image);

			}
		}

	}

}
