package com.secondline.songwriter.model;

public class Word {

	private String stringValue;
	private PartsOfSpeech pos;
	private Syllable[] syllables;

	public Word(String word) {
		stringValue = word;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public PartsOfSpeech getPos() {
		return pos;
	}

	public void setPos(PartsOfSpeech pos) {
		this.pos = pos;
	}

	public Syllable[] getSyllables() {
		return syllables;
	}

	public void setSyllables(Syllable[] syllables) {
		this.syllables = syllables;
	}

}
