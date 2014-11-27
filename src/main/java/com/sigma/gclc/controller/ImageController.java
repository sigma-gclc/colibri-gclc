package com.sigma.gclc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/images")
public class ImageController {

	private File imageDirectory;
	private Logger logger = LoggerFactory.getLogger(getClass());
	int imageCachePeriod;

	@Value("${imagesDirectory}")
	public void setImagesDirectory(File directory) {
		this.imageDirectory = directory;
	}
	@Value("${image.cachePeriod:36000}")
	public void setImageCachePeriod(int cachePeriod) {
		this.imageCachePeriod= cachePeriod;
	}
	
	@RequestMapping(value="{img}")
	public void loadImage(@PathVariable("img")String img, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(img)) {
			response.sendError(404);
			return;
		}
		
		File imgFile = new File(imageDirectory, img);
		if (!imgFile.exists()) {
			response.sendError(404);
			logger.warn(" Fichier: {} non trouvé", imgFile);
			return;
		}
		//response.setContentLength();
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		response.addHeader("Cache-Control", MessageFormat.format("public, max-age={0}", imageCachePeriod));
		try (InputStream input = new FileInputStream(imgFile)){
		  response.getOutputStream().write(IOUtils.toByteArray(input));	
		} 
		
	}
}
