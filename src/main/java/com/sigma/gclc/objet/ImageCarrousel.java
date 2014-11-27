package com.sigma.gclc.objet;

public class ImageCarrousel {
	
	private String nom;
	private int id;
	private String description;
	
	public ImageCarrousel(String nom) {
		super();
		this.nom = nom;
	}
	
	public ImageCarrousel(String nom, int id) {
		super();
		this.nom = nom;
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
