package com.secondline.songwriter.model;

public class Lyric {

	private String lyrics;
	private Integer[] stresses;
	private String[] rhymes;

	public Lyric(String lyrics) {
		this.setLyrics(lyrics);
	}

	public Integer[] getStresses() {
		return stresses;
	}

	public void setStresses(Integer[] integers) {
		this.stresses = integers;
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
