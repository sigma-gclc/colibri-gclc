package com.sigma.gclc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/backoffice/upload")
public class UploadController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${uploadImagesDirectory}")
	private File uploadImagesDirectory;

	@RequestMapping(produces=MediaType.TEXT_HTML_VALUE)
	public String uploadForm() {
		return "upload";
	}
	
	@PostConstruct
	public void postInit() {
		if (!uploadImagesDirectory.exists()) {
			if (!uploadImagesDirectory.mkdirs()) {
				logger.warn(MessageFormat.format("Impossible de créer le répertoire {0}", uploadImagesDirectory));
				//throw new IllegalArgumentException();
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public String upload(@RequestParam(value="name", required=false) String name, @RequestParam(value="file", required=true) MultipartFile file) throws Exception {
		
		if (StringUtils.isBlank(name)) {
			name = file.getOriginalFilename();
		}
		if (StringUtils.isBlank(name)) {
			name = "Nom de fichier non déterminé";
		}
		
		if (file.isEmpty()) {
			logger.info("Fichier {} vide", name);
			throw new IllegalArgumentException(MessageFormat.format("Fichier {} est vide", name));
		}
		
		if (!uploadImagesDirectory.exists() && !uploadImagesDirectory.mkdirs()) {
			throw new IllegalArgumentException(MessageFormat.format("Impossible de créer le répertoire {0}", uploadImagesDirectory));
		}
		
		File uploadedFile = new File(uploadImagesDirectory, name);
		try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(uploadedFile)); BufferedInputStream input=new BufferedInputStream(file.getInputStream())) {
			IOUtils.copy(input, output);
			output.flush();
		}
		
		return "upload-success";
		
	}
}
