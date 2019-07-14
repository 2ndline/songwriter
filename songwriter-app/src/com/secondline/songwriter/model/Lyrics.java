package com.secondline.songwriter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lyrics - the lyrical content of a section. Each section's lyrics will have
 * rhyme patterns, stress patterns, in addition to the actual words. Each line
 * of a section is considered a Lyric.
 * 
 * @author Bob
 * 
 */
public class Lyrics {
	String[] rhymePattern;
	String[] stressPattern;
	List<Lyric> lyrics = new ArrayList<Lyric>();

	public Lyrics() {
	}

	public String[] getRhymePattern() {
		return rhymePattern;
	}

	public void setRhymePattern(String[] rhymePattern) {
		this.rhymePattern = rhymePattern;
	}

	public String[] getStressPattern() {
		return stressPattern;
	}

	public void setStressPattern(String[] stressPattern) {
		this.stressPattern = stressPattern;
	}

	public List<Lyric> getLyrics() {
		return lyrics;
	}

	public void setLyrics(List<Lyric> lyrics) {
		this.lyrics = lyrics;
	}

}
