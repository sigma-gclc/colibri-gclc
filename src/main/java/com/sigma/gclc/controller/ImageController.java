package com.sigma.gclc.controller;

import static java.text.MessageFormat.format;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.IMAGE_GIF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.MediaTypeFileExtensionResolver;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/images")
public class ImageController {

	@Value("${mobileImageDirectory}")
	private File mobileImageDirectory;

	@Value("${desktopImageDirectory}")
	private File desktopImageDirectory;

	private Logger logger = LoggerFactory.getLogger(getClass());
	int imageCachePeriod;

	@Autowired
	private MediaTypeFileExtensionResolver mediaTypeFileExtensionResolver;

	@Value("${image.cachePeriod:36000}")
	public void setImageCachePeriod(int cachePeriod) {
		this.imageCachePeriod = cachePeriod;
	}

	@RequestMapping(value = "{img:.+}")
	public ResponseEntity<byte[]> loadImage(@PathVariable("img") String img,
			Device device) throws Exception {
		if (StringUtils.isBlank(img)) {
			return new ResponseEntity<byte[]>(
					"Nom de l'image non spécifié".getBytes("UTF-8"),
					HttpStatus.BAD_REQUEST);
		}

		File imgFile = new File(desktopImageDirectory, img);

		if (device.isMobile()) {
			imgFile = new File(mobileImageDirectory, img);
		}
		if (!imgFile.exists()) {
			logger.warn(" Fichier '{}' non trouvé", imgFile);
			return new ResponseEntity<byte[]>(format("Immage {0} non trouvé",
					imgFile.getName()).getBytes("UTF-8"), HttpStatus.NOT_FOUND);
		}

		HttpHeaders headers = new HttpHeaders();

		headers.setCacheControl(MessageFormat.format("public, max-age={0}",
				imageCachePeriod));
		headers.setContentType(findImageMediaType(imgFile));
		// alimente la reponse http
		try (InputStream input = new FileInputStream(imgFile)) {

			return new ResponseEntity<byte[]>(IOUtils.toByteArray(input),
					headers, HttpStatus.OK);
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
