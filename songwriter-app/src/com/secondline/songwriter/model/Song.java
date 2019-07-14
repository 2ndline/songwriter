package com.secondline.songwriter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Song - a class representing an entire song.
 * 
 * @author Bob
 * 
 */
public class Song {
	String title;
	List<Section> sections = new ArrayList<Section>();

	public Song() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Title: " + title + "\n");
		output.append("========================\n");
		for (Section section : sections) {
			output.append("Section: " + section.getTitle() + "\n");
			output.append("----\n");
			for(Lyric lyric : section.getLyrics().getLyrics()){
				output.append(lyric.getLyrics()+"\n");
			}
			output.append("\n");
		}
		return output.toString();
	}

}
