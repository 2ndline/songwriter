package com.secondline.songwriter.model;

public class Syllable {

	private String value;
	private Boolean stressed;

	public Syllable(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean isStressed() {
		return stressed;
	}

	public void setStressed(Boolean stressed) {
		this.stressed = stressed;
	}
}
