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
	Integer[] rhymePattern;
	List<Lyric> lyrics = new ArrayList<Lyric>();

	public Lyrics() {
	}

	public Integer[] getRhymePattern() {
		return rhymePattern;
	}
	
	public String getRhymePatternPretty(){
		StringBuilder sb = new StringBuilder();
		for(Integer rhyme : rhymePattern){
			if(rhyme < 0)
				sb.append("X ");
			else{
				char rhymeChar = 'A';
				rhymeChar+= rhyme;
				sb.append(Character.toString(rhymeChar) + " ");
			}
		}
		return sb.toString().trim();
	}

	public void setRhymePattern(Integer[] rhymePattern2) {
		this.rhymePattern = rhymePattern2;
	}

	public List<Lyric> getLyrics() {
		return lyrics;
	}

	public void setLyrics(List<Lyric> lyrics) {
		this.lyrics = lyrics;
	}

}
