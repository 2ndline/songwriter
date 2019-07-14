package com.secondline.songwriter.model;

/**
 * A section of a song. Each section may have lyrics and/or music. 
 * @author Bob
 *
 */
public class Section {

	private String title;
	private Lyrics lyrics = new Lyrics();
	private Music music = new Music();

	public Section() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Lyrics getLyrics() {
		return lyrics;
	}

	public void setLyrics(Lyrics lyrics) {
		this.lyrics = lyrics;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	};

}
