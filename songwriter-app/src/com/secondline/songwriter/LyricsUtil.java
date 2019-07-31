package com.secondline.songwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import rita.RiLexicon;
import rita.RiString;
import rita.RiTa;
import rita.support.Phoneme;

import com.secondline.songwriter.model.Lyric;
import com.secondline.songwriter.model.Lyrics;
import com.secondline.songwriter.model.PartsOfSpeech;
import com.secondline.songwriter.model.Section;
import com.secondline.songwriter.model.Song;
import com.secondline.songwriter.model.Syllable;
import com.secondline.songwriter.model.Word;
import com.secondline.songwriter.model.analysis.LyricsAnalysis;
import com.secondline.songwriter.model.analysis.LyricsAnalysis.Balance;

/**
 * LyricsUtil - a utility class for computing lyrical measurements like rhyme
 * and stress.
 * 
 * @author Bob
 * 
 */
public class LyricsUtil {

	private static RiLexicon lexicon = new RiLexicon();

	final static Logger log = Logger.getLogger(LyricsUtil.class.getName());

	public static Song getSongFromLyricsFile(String filename)
			throws FileNotFoundException {

		if (filename == null || filename.trim().isEmpty())
			return null;

		Song song = new Song();
		Scanner inFile = new Scanner(new File(filename));
		Section lastSection = null;

		while (inFile.hasNextLine()) {
			String fileLine = inFile.nextLine();
			if (fileLine.startsWith("T: ")) {
				song.setTitle(fileLine.replace("T: ", ""));
			} else if (fileLine.startsWith("S: ")) {
				// new section
				lastSection = new Section();
				lastSection.setTitle(fileLine.replace("S: ", ""));
			} else if (fileLine.trim().isEmpty()) {
				// end of section
				if (lastSection != null) {
					computePatterns(lastSection.getLyrics());
					song.getSections().add(lastSection);
					lastSection = null;
				}
			} else {
				// lyrics
				lastSection.getLyrics().getLyrics().add(getLyrics(fileLine));
			}
		}
		inFile.close();
		if (lastSection != null) {
			computePatterns(lastSection.getLyrics());
			song.getSections().add(lastSection);
		}

		return song;
	}

	private static void computePatterns(Lyrics lyrics) {
		List<Lyric> lyricList = lyrics.getLyrics();
		// first, rhyme patterns
		Integer[] rhymePattern = new Integer[lyricList.size()];
		Map<Integer, Set<String>> rhymeSetMap = new HashMap<Integer, Set<String>>();

		// get last word & rhymes for each lyric
		List<String> lastWords = new ArrayList<String>();
		List<Set<String>> rhymeSets = new ArrayList<Set<String>>();

		for (Lyric lyric : lyricList) {
			rhymeSets
					.add(new HashSet<String>(Arrays.asList(lyric.getRhymes())));
			String lyricString = lyric.getLyrics();
			lyricString = lyricString.replaceAll("[?.,!-]", "").trim();
			String lastWord = lyricString.substring(lyricString
					.lastIndexOf(" ") + 1);
			lastWords.add(lastWord);
		}

		int rhymeCount = -1;
		for (int i = 0; i < rhymePattern.length; ++i) {
			if (rhymePattern[i] != null)
				continue;

			rhymeCount++;
			String lastWord = lastWords.get(i);
			boolean found = false;
			for (int j = 0; j < rhymeSets.size(); ++j) {
				if (j == i)
					continue;
				if (rhymePattern[j] != null) {
					continue;
				}

				Set<String> rhymeSet = rhymeSets.get(j);
				if (rhymeSet.contains(lastWord)) {
					found = true;

					if (i < j) {
						// rhymePattern[i] is being repeated at rhymePattern[j]
						if (rhymePattern[i] == null) {
							rhymePattern[i] = rhymeCount;
							rhymeSet.add(lastWord);
							rhymeSetMap.put(rhymeCount, rhymeSet);
						}
						rhymePattern[j] = rhymePattern[i];
					}
				}
			}
			if (!found)
				rhymePattern[i] = -1;

		}
		lyrics.setRhymePattern(rhymePattern);
		// next, stress patterns
	}

	public static List<Lyric> getLyricsFromFile(String filename)
			throws FileNotFoundException {

		List<String> lyricsList = new ArrayList<String>();
		Scanner inFile = new Scanner(new File(filename));
		while (inFile.hasNextLine()) {
			lyricsList.add(inFile.nextLine());
		}
		inFile.close();

		List<Lyric> result = new ArrayList<Lyric>();
		for (String lyric : lyricsList) {
			result.add(getLyrics(lyric));
		}
		return result;
	}

	public static Lyric getLyrics(String lyrics) {
		Lyric lyric = new Lyric(lyrics.trim());
		String[] wordStrings = RiTa.stripPunctuation(lyrics).trim().split(" ");
		Word[] words = new Word[wordStrings.length];
		for(int i = 0; i < wordStrings.length; ++i){
			String wordString = wordStrings[i];
			Word word = getWord(wordString);
			words[i] = word;
		}
		lyric.setWords(words);
		String lastWord = words[words.length-1].getStringValue();
		lyric.setRhymes(lexicon.rhymes(lastWord));
		if (lyric.getRhymes().length == 0)
			log.warning("~~~WARNING~~ Last word " + lastWord
					+ " has no rhymes!");
		return lyric;
	}

	private static Word getWord(String wordString) {
		Word word = new Word(wordString);
		RiString rs = new RiString(wordString);
		PartsOfSpeech ps = PartsOfSpeech.valueOf(rs.getFeature("pos"));
		String[] syllablesStrings = rs.getFeature("syllables").split("/");
		Boolean[] stresses = getStresses(wordString);
		Syllable[] syllables = new Syllable[syllablesStrings.length];
		for(int i = 0; i < syllablesStrings.length; ++i){
			String syllableString = syllablesStrings[i];
			Syllable syllable = new Syllable(syllableString);
			syllable.setStressed(stresses[i]);
			int sonality = Phoneme.getSonority(syllableString);
			syllables[i] = syllable;
		}
		word.setSyllables(syllables);
		return word;
	}

	private static Boolean[] getStresses(String lyrics) {
		log.fine("Getting stresses for: " + lyrics);
		if (lyrics.trim().isEmpty())
			return new Boolean[] {};

		// strip grammar not needed for computation
		lyrics = lyrics.replaceAll("[?.,!-]", "");
		RiString rs = new RiString(lyrics);
		rs.analyze();

		String stresses = rs.getFeature("stresses");
		String[] wordValues = lyrics.split("[ ,]");
		String[] stressValues = stresses.split("[ ,]");

		log.fine("Word values: " + Arrays.toString(wordValues));
		log.fine("Stress values: " + Arrays.toString(stressValues));
		stressValues = cleanArrays(stressValues);
		log.fine("Stress values after cleaning: "
				+ Arrays.toString(stressValues));

		int arrayLen = stresses.split("[ ,/]").length;
		log.fine("Array len " + arrayLen);
		List<Boolean> stressList = new ArrayList<Boolean>();
		for (int i = 0; i < wordValues.length; ++i) {
			String word = wordValues[i];
			String fullWord = getFullWord(word);

			if (fullWord != word) {
				log.fine("Shortened word: " + fullWord);
				stressList.add(getStressMonoSyllable(fullWord));
				log.fine("Calculated stress: "
						+ stressList.get(stressList.size() - 1));
			} else if (stressValues[i].contains("/")) {
				// multi-syllabic
				log.fine("Polysyllabic word: " + fullWord);
				String[] wordStress = stressValues[i].split("/");
				for (int j = 0; j < wordStress.length; ++j) {
					stressList.add(Integer.parseInt(wordStress[j]) == 1);
					log.fine("Calculated stress: "
							+ stressList.get(stressList.size() - 1));
				}
			} else {
				log.fine("Monosyllabic word: " + word);
				stressList.add(getStressMonoSyllable(word));
				log.fine("Calculated stress: "
						+ stressList.get(stressList.size() - 1));
			}
		}

		Boolean[] result = new Boolean[stressList.size()];
		result = stressList.toArray(result);
		log.fine("Returning: " + Arrays.toString(result));
		return result;
	}

	private static String[] cleanArrays(String[] stressValues) {
		List<String> stresses = new ArrayList<String>(
				Arrays.asList(stressValues));
		String[] result = new String[stresses.size()];

		int count = 0;
		for (String stress : stresses) {
			if (stress.equals("'"))
				continue;
			result[count] = stress;
			count++;
		}
		result = Arrays.copyOf(result, count);
		return result;
	}

	private static Boolean getStressMonoSyllable(String word) {

		RiString rs = new RiString(word);
		rs.analyze();
		String pos = rs.getPosAt(0, false);
		log.fine("Word "+word+ " POS: " + pos);
		PartsOfSpeech ps = PartsOfSpeech.valueOf(pos);
		if (ps != null)
			return (ps.getStress() == 1);
		return null;

	}

	/**
	 * This method is for any slang terms in order to figure out what part of
	 * speech the slang word represents. This is a static list for exceptions
	 * 
	 * @param word
	 * @return
	 */
	private static String getFullWord(String word) {
		if (word == null)
			return null;

		if (word.equalsIgnoreCase("'Cause"))
			return "because";

		return word;
	}

	public static LyricsAnalysis analyzeLyrics(Lyrics lyrics) {
	
		if(lyrics == null || lyrics.getLyrics() == null || lyrics.getLyrics().isEmpty())
			return null;
		
		LyricsAnalysis result = new LyricsAnalysis();

		result.setBalance(lyrics.getLyrics().size() % 2 == 0 ? Balance.SYMMETRICAL : Balance.ASYMMETRICAL);
		return result;
	}

}
