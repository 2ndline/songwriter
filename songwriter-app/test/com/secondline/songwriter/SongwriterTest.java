package com.secondline.songwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import junit.framework.Assert;

import org.junit.Test;

import com.secondline.songwriter.model.Lyric;

import rita.RiLexicon;
import rita.RiString;

public class SongwriterTest {

	@Test
	public void testLyricsUtil() throws FileNotFoundException {

		List<String> lyricsList = new ArrayList<String>();
		List<String> stressesList = new ArrayList<String>();
		List<String> rhymesList = new ArrayList<String>();
		
		Scanner inFile = new Scanner(new File("test/resources/icantfight.txt"));
		while (inFile.hasNextLine()) {
			lyricsList.add(inFile.nextLine());
		}
		inFile.close();

		inFile = new Scanner(new File("test/resources/icantfight-stresses.txt"));
		while (inFile.hasNextLine()) {
			stressesList.add(inFile.nextLine());
		}
		inFile.close();

		inFile = new Scanner(new File("test/resources/icantfight-rhymes.txt"));
		while (inFile.hasNextLine()) {
			rhymesList.add(inFile.nextLine());
		}
		inFile.close();

		for (int i = 0; i < lyricsList.size(); ++i) {
			String lyricsString = lyricsList.get(i);
			String expectedStresses = stressesList.get(i);

			Lyric lyrics = LyricsUtil.getLyrics(lyricsString);
			Assert.assertNotNull(lyrics);

			Assert.assertEquals("Index: "+i, expectedStresses,
					Arrays.toString(lyrics.getStresses()));

			// TODO calc rhyme pattern, compare to rhymesList
			String expectedRhymes = rhymesList.get(i);

		}
	}

	@Test
	public void testRiFeatures() {
		RiString rs = new RiString(
				"And you can tell everybody, this is your song");

		Map<String, String> features = rs.features();
		Assert.assertNotNull(features);
		for (String feature : features.keySet()) {
			String value = features.get(feature);
			Assert.assertNotNull(value);
			System.out.println("Feature: " + feature + ", value: " + value);
		}

		String lyrics = rs.get("text");
		Assert.assertNotNull(lyrics);
		String lastWord = lyrics.substring(lyrics.lastIndexOf(" ") + 1);
		RiLexicon lexicon = new RiLexicon();
		String[] rhymes = lexicon.rhymes(lastWord);
		Assert.assertNotNull(rhymes);
		Assert.assertTrue(rhymes.length > 0);
		Assert.assertTrue(Arrays.asList(rhymes).contains("long"));
	}

}
