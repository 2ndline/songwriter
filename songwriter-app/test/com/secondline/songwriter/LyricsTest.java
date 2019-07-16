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
import com.secondline.songwriter.model.Section;
import com.secondline.songwriter.model.Song;

import rita.RiLexicon;
import rita.RiString;

public class LyricsTest {

	@Test
	public void testLyricsUtil() throws FileNotFoundException {
		// expected values
		List<String> stressesList = new ArrayList<String>();

		Scanner inFile = new Scanner(new File(
				"test/resources/icantfight-stresses.txt"));
		while (inFile.hasNextLine()) {
			stressesList.add(inFile.nextLine());
		}
		inFile.close();

		// get the lyrics from the file
		List<Lyric> lyricsList = LyricsUtil
				.getLyricsFromFile("test/resources/icantfight.txt");
		for (int i = 0; i < lyricsList.size(); ++i) {

			// check the stresses computed for the lyrics
			String expectedStresses = stressesList.get(i);
			Lyric lyrics = lyricsList.get(i);
			Assert.assertNotNull(lyrics);

			Assert.assertEquals("Index: " + i, expectedStresses,
					Arrays.toString(lyrics.getStresses()));
		}
	}

	@Test
	public void testSongLyricsFromFile() throws FileNotFoundException {
		Song song = LyricsUtil
				.getSongFromLyricsFile("test/resources/careforgot-lyrics.txt");
		Assert.assertNotNull(song);

		Assert.assertEquals("City That Care Forgot", song.getTitle());
		Assert.assertEquals(6, song.getSections().size());
		System.out.println(song.toString());

		Section intro = song.getSections().get(0);
		Assert.assertEquals(4, intro.getLyrics().getLyrics().size());
		Assert.assertEquals("[0, 0, 0, 0]",
				Arrays.toString(intro.getLyrics().getRhymePattern()));
		Assert.assertEquals("A A A A", intro.getLyrics()
				.getRhymePatternPretty());
		List<String> stressPatterns = Arrays.asList(new String[] {
				"` u ` ` ` u ` u`",

				"` ` ` u u u`",

				"u ` u ` `u ` ` `",

				"` u `u ` ` u `" });
		for (int i = 0; i < intro.getLyrics().getLyrics().size(); ++i) {
			Assert.assertEquals("Index " + i, stressPatterns.get(i), intro
					.getLyrics().getLyrics().get(i).getStressesPretty());
		}

		Section verseOne = song.getSections().get(1);
		Assert.assertEquals(4, verseOne.getLyrics().getLyrics().size());
		Assert.assertEquals("A A B B", verseOne.getLyrics()
				.getRhymePatternPretty());

		Section verseTwo = song.getSections().get(2);
		Assert.assertEquals(4, verseTwo.getLyrics().getLyrics().size());
		Assert.assertEquals("A A B B", verseTwo.getLyrics()
				.getRhymePatternPretty());

		Section bridge = song.getSections().get(3);
		Assert.assertEquals(6, bridge.getLyrics().getLyrics().size());
		Assert.assertEquals("A B A B B B", bridge.getLyrics()
				.getRhymePatternPretty());

		Section danceBreak = song.getSections().get(4);
		Assert.assertEquals(0, danceBreak.getLyrics().getLyrics().size());

		Section verseThree = song.getSections().get(5);
		Assert.assertEquals(6, verseThree.getLyrics().getLyrics().size());
		Assert.assertEquals("A A B B B B", verseThree.getLyrics()
				.getRhymePatternPretty());
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
