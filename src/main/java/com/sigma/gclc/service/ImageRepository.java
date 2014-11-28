package com.sigma.gclc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sigma.gclc.objet.ImageCarrousel;

@Component
public class ImageRepository {

	private List<ImageCarrousel> images;

	@Autowired
	public ImageRepository(
			@Value("${imageRepositoryInitSize:10}") int imageRepositoryInitSize) {
		this.images = Collections
				.synchronizedList(new ArrayList<ImageCarrousel>(
						imageRepositoryInitSize));
	}

	public List<ImageCarrousel> getImages() {
		return images;
	}

	public void addImage(ImageCarrousel image) {
		this.images.add(image);
	}

	public void reset() {
		this.images.clear();
	}

}
