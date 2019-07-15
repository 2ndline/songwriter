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
		//expected values
		List<String> stressesList = new ArrayList<String>();
		List<String> rhymesList = new ArrayList<String>();
		
		Scanner inFile = new Scanner(new File("test/resources/icantfight-stresses.txt"));
		while (inFile.hasNextLine()) {
			stressesList.add(inFile.nextLine());
		}
		inFile.close();

		inFile = new Scanner(new File("test/resources/icantfight-rhymes.txt"));
		while (inFile.hasNextLine()) {
			rhymesList.add(inFile.nextLine());
		}
		inFile.close();

		//get the lyrics from the file
		List<Lyric> lyricsList = LyricsUtil.getLyricsFromFile("test/resources/icantfight.txt");
		for (int i = 0; i < lyricsList.size(); ++i) {
			
			//check the stresses computed for the lyrics
			String expectedStresses = stressesList.get(i);
			Lyric lyrics = lyricsList.get(i);
			Assert.assertNotNull(lyrics);

			Assert.assertEquals("Index: "+i, expectedStresses,
					Arrays.toString(lyrics.getStresses()));

			// TODO calc rhyme pattern, compare to rhymesList
			String expectedRhymes = rhymesList.get(i);

		}
	}
	
	@Test
	public void testSongLyricsFromFile() throws FileNotFoundException{
		Song song = LyricsUtil.getSongFromLyricsFile("test/resources/careforgot-lyrics.txt");
		Assert.assertNotNull(song);
		
		Assert.assertEquals("City That Care Forgot", song.getTitle());
		Assert.assertEquals(6, song.getSections().size());
		System.out.println(song.toString());

		Section intro = song.getSections().get(0);
		Assert.assertEquals(4, intro.getLyrics().getLyrics().size());
		Assert.assertEquals("[0, 0, 0, 0]", Arrays.toString(intro.getLyrics().getRhymePattern()));
		
		Section verseOne = song.getSections().get(1);
		Assert.assertEquals(4, verseOne.getLyrics().getLyrics().size());
		Assert.assertEquals("[0, 0, 1, 1]", Arrays.toString(verseOne.getLyrics().getRhymePattern()));

		Section verseTwo = song.getSections().get(2);
		Assert.assertEquals(4, verseTwo.getLyrics().getLyrics().size());
		Assert.assertEquals("[0, 0, 1, 1]", Arrays.toString(verseTwo.getLyrics().getRhymePattern()));

		Section bridge = song.getSections().get(3);
		Assert.assertEquals(6, bridge.getLyrics().getLyrics().size());
		Assert.assertEquals("[0, 1, 0, 1, 1, 1]", Arrays.toString(bridge.getLyrics().getRhymePattern()));

		Section danceBreak = song.getSections().get(4);
		Assert.assertEquals(0, danceBreak.getLyrics().getLyrics().size());

		Section verseThree = song.getSections().get(5);
		Assert.assertEquals(6, verseThree.getLyrics().getLyrics().size());
		Assert.assertEquals("[0, 0, 1, 1, 1, 1]", Arrays.toString(verseThree.getLyrics().getRhymePattern()));
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
