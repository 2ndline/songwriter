package com.secondline.songwriter.model;

public class Lyric {

	private String lyrics;
	private Boolean[] stresses;
	private String[] rhymes;

	public Lyric(String lyrics) {
		this.setLyrics(lyrics);
	}

	public Boolean[] getStresses() {
		return stresses;
	}

	public String getStressesPretty(){
		StringBuilder sb = new StringBuilder();
		for(Boolean stress : stresses){
		    if(stress == null)
		    	sb.append("X ");
			else if(stress)
				sb.append("` ");
			else
				sb.append("u ");
		}
		return sb.toString().trim();
	}
	public void setStresses(Boolean[] booleans) {
		this.stresses = booleans;
	}

	public String[] getRhymes() {
		return rhymes;
	}

	public void setRhymes(String[] rhymes) {
		this.rhymes = rhymes;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

}
