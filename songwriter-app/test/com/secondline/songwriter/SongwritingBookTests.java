package com.secondline.songwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import com.secondline.songwriter.model.Lyric;
import com.secondline.songwriter.model.Lyrics;
import com.secondline.songwriter.model.Section;
import com.secondline.songwriter.model.Song;
import com.secondline.songwriter.model.analysis.LyricsAnalysis;
import com.secondline.songwriter.model.analysis.LyricsAnalysis.Balance;

import rita.RiLexicon;
import rita.RiString;

public class SongwritingBookTests {

	@Test
	public void chapterOneBalanceTest() throws FileNotFoundException {
		// get the lyrics from the file
		Song song = LyricsUtil.getSongFromLyricsFile("test/resources/songwritingbookfiles/ch1.txt");
		
		Assert.assertNotNull(song);
		Lyrics lyrics = song.getSections().get(0).getLyrics();
		List<Lyric> lyricsList =lyrics.getLyrics();
		//four phrases
		Assert.assertEquals(4, lyricsList.size());
		//lyrics are balanced
		Assert.assertEquals(0, lyricsList.size() % 2);
		
		LyricsAnalysis analysis = LyricsUtil.analyzeLyrics(lyrics);
		Assert.assertNotNull(analysis);
		Assert.assertEquals(Balance.SYMMETRICAL, analysis.getBalance());
	}

	@Test
	public void chapterTwoPaceTest() throws FileNotFoundException {
		//TODO load in files
		
		//TODO run analysis
		
		//TODO assert pace is appropriate to the file
	}

}
