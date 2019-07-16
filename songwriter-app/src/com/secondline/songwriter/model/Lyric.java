package com.secondline.songwriter.model;

import java.util.ArrayList;

public class Lyric {

	private String lyrics;
	private Word[] words;
	private String[] rhymes;

	public Lyric(String lyrics) {
		this.setLyrics(lyrics);
	}

	public Boolean[] getStresses() {
		if((words != null && words.length > 0)){
			ArrayList<Boolean> stresses = new ArrayList<Boolean>();
			for(Word word : words){
				for(Syllable stress : word.getSyllables()){
					stresses.add(stress.isStressed());
				}
			}
			Boolean[] result = new Boolean[stresses.size()];
			return stresses.toArray(result);
		}else
			return null;
	}

	public String getStressesPretty(){
		StringBuilder sb = new StringBuilder();
		if((words != null && words.length > 0)){
			for(Word word : words){
				for(Syllable syllable : word.getSyllables()){
					Boolean stress = syllable.isStressed();
					if(stress == null)
				    	sb.append("X");
					else if(stress)
						sb.append("`");
					else
						sb.append("u");
				}
				sb.append(" ");
			}
			return sb.toString().trim();
		}else
			return null;
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

	public Word[] getWords() {
		return words;
	}

	public void setWords(Word[] words) {
		this.words = words;
	}

}
