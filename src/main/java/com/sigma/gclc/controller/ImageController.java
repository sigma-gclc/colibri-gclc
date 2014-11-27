package com.sigma.gclc.controller;

import static java.text.MessageFormat.format;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.IMAGE_GIF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.MediaTypeFileExtensionResolver;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/images")
public class ImageController {

	private File imageDirectory;
	private Logger logger = LoggerFactory.getLogger(getClass());
	int imageCachePeriod;
	
	@Autowired
	private MediaTypeFileExtensionResolver mediaTypeFileExtensionResolver;

	@Value("${imagesDirectory}")
	public void setImagesDirectory(File directory) {
		this.imageDirectory = directory;
	}
	@Value("${image.cachePeriod:36000}")
	public void setImageCachePeriod(int cachePeriod) {
		this.imageCachePeriod= cachePeriod;
	}
	
	@RequestMapping(value="{img:.+}")
	public ResponseEntity<byte[]> loadImage(@PathVariable("img")String img) throws Exception {
		if (StringUtils.isBlank(img)) {
			return new ResponseEntity<byte[]>("Nom de l'image non spécifié".getBytes("UTF-8"), HttpStatus.BAD_REQUEST);
		}
		
		File imgFile = new File(imageDirectory, img);
		if (!imgFile.exists()) {
			logger.warn(" Fichier '{}' non trouvé", imgFile);
			return new ResponseEntity<byte[]>(format("Immage {0} non trouvé", imgFile.getName()).getBytes("UTF-8"), HttpStatus.NOT_FOUND);
		}
		
		HttpHeaders headers = new HttpHeaders();
		
		ResponseEntity<byte[]> responseEntity = null;
		
		headers.setCacheControl(MessageFormat.format("public, max-age={0}", imageCachePeriod));
		headers.setContentType(findImageMediaType(imgFile));
		
		try (InputStream input = new FileInputStream(imgFile)){
			return new ResponseEntity(IOUtils.toByteArray(input), headers, HttpStatus.OK);
		} 
	}
	
	MediaType findImageMediaType(File file) {
		String extension = FilenameUtils.getExtension(file.getName());
		MediaType mediaType = null;
		
		switch (extension) {
		case "png": 
			mediaType = IMAGE_PNG;
			break;
		case "jpg":
			mediaType = IMAGE_JPEG;
		case "jpeg":
			mediaType = IMAGE_JPEG;
		case "gif":
			mediaType = IMAGE_GIF;
		default:
			logger.info("Extension {} non connue", extension);
			mediaType = APPLICATION_OCTET_STREAM;
			break;
		}
		return mediaType;
	}
}
