package com.secondline.songwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import rita.RiLexicon;
import rita.RiString;

import com.secondline.songwriter.model.Lyric;
import com.secondline.songwriter.model.Lyrics;
import com.secondline.songwriter.model.Section;
import com.secondline.songwriter.model.Song;

/**
 * LyricsUtil - a utility class for computing lyrical measurements like rhyme
 * and stress.
 * 
 * @author Bob
 * 
 */
public class LyricsUtil {

	private static RiLexicon lexicon = new RiLexicon();

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
		if (lastSection != null)
			song.getSections().add(lastSection);

		return song;
	}

	private static void computePatterns(Lyrics lyrics) {
		List<Lyric> lyricList = lyrics.getLyrics();
		// first, rhyme patterns
		String[] rhymePattern = new String[lyricList.size()];

		// get last word & rhymes for each lyric
		List<String> lastWords = new ArrayList<String>();
		List<Set<String>> rhymeSets = new ArrayList<Set<String>>();

		for (Lyric lyric : lyricList) {
			rhymeSets
					.add(new HashSet<String>(Arrays.asList(lyric.getRhymes())));
			lastWords.add(lyric.getLyrics().substring(
					lyric.getLyrics().replaceAll("[?.,!-]", "").lastIndexOf(" ") + 1));
		}

		int rhymeCount = 0;
		for (int i = 0; i < rhymePattern.length; ++i) {
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
						if (rhymePattern[i] == null)
							rhymePattern[i] = "" + ('A' + rhymeCount++);
						rhymePattern[j] = rhymePattern[i];
					}
				}
			}
			if (!found)
				rhymePattern[i] = "X";

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
		Lyric lyric = new Lyric(lyrics);
		lyric.setStresses(getStresses(lyrics));
		String lastWord = lyrics.substring(lyrics.lastIndexOf(" ") + 1);
		lyric.setRhymes(lexicon.rhymes(lastWord));
		return lyric;
	}

	private static Integer[] getStresses(String lyrics) {
		System.out.println("Getting stresses for: " + lyrics);
		if (lyrics.trim().isEmpty())
			return new Integer[] { -1 };

		// strip grammar not needed for computation
		lyrics = lyrics.replaceAll("[?.,!-]", "");
		RiString rs = new RiString(lyrics);
		rs.analyze();

		String stresses = rs.getFeature("stresses");
		String[] wordValues = lyrics.split("[ ,]");
		String[] stressValues = stresses.split("[ ,]");

		System.out.println("Word values: " + Arrays.toString(wordValues));
		System.out.println("Stress values: " + Arrays.toString(stressValues));
		stressValues = cleanArrays(stressValues);
		System.out.println("Stress values after cleaning: "
				+ Arrays.toString(stressValues));

		int arrayLen = stresses.split("[ ,/]").length;
		System.out.println("Array len " + arrayLen);
		List<Integer> stressList = new ArrayList<Integer>();
		for (int i = 0; i < wordValues.length; ++i) {
			String word = wordValues[i];
			String fullWord = getFullWord(word);

			if (fullWord != word) {
				System.out.println("Shortened word: " + fullWord);
				stressList.add(getStressMonoSyllable(fullWord));
				System.out.println("Calculated stress: "
						+ stressList.get(stressList.size() - 1));
			} else if (stressValues[i].contains("/")) {
				// multi-syllabic
				System.out.println("Polysyllabic word: " + fullWord);
				String[] wordStress = stressValues[i].split("/");
				for (int j = 0; j < wordStress.length; ++j) {
					stressList.add(Integer.parseInt(wordStress[j]));
					System.out.println("Calculated stress: "
							+ stressList.get(stressList.size() - 1));
				}
			} else {
				System.out.println("Monosyllabic word: " + word);
				stressList.add(getStressMonoSyllable(word));
				System.out.println("Calculated stress: "
						+ stressList.get(stressList.size() - 1));
			}
		}

		Integer[] result = new Integer[stressList.size()];
		result = stressList.toArray(result);
		System.out.println("Returning: " + Arrays.toString(result));
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

	private enum PartsOfSpeech {
		// for single-syllable words:

		// nouns, verbs, adjectives, adverbs are stressed
		// prepositions, articles, conj, aux verbs indicating tense/mood,
		// personal pronouns, relative pronouns are not

		cc("Coordinating conjunction", 0), cd("Cardinal number", 0), dt(
				"Determiner", 0), ex("Existential there", 0), fw(
				"Foreign word", 0), in(
				"Preposition or subordinating conjunction", 1), jj("Adjective",
				1), jjr("Adjective, comparative", 1), jjs(
				"Adjective, superlative", 1), ls("List item marker", 0), md(
				"Modal", 0), nn("Noun, singular or mass", 1), nns(
				"Noun, plural", 1), nnp("Proper noun, singular", 1), nnps(
				"Proper noun, plural", 1), pdt("Predeterminer", 0), pos(
				"Possessive ending", 0), prp("Personal pronoun", 0), prp$(
				"Possessive pronoun", 0), rb("Adverb", 1), rbr(
				"Adverb, comparative", 1), rbs("Adverb, superlative", 1), rp(
				"Particle", 0), sym("Symbol", 0), to("to", 0), uh(
				"Interjection", 0), vb("Verb, base form", 1), vbd(
				"Verb, past tense", 1), vbg(
				"Verb, gerund or present participle", 1), vbn(
				"Verb, past participle", 1), vbp(
				"Verb, non-3rd person singular present", 1), vbz(
				"Verb, 3rd person singular present", 1), wdt("Wh-determiner", 0), wp(
				"Wh-pronoun", 0), wp$("Possessive wh-pronoun", 0), wrb(
				"Wh-adverb", 0);

		private int stress;

		PartsOfSpeech(String desc, int stress) {
			this.stress = stress;
		}

		public int getStress() {
			return stress;
		}

	}

	private static int getStressMonoSyllable(String word) {

		RiString rs = new RiString(word);
		rs.analyze();
		String pos = rs.getPosAt(0, false);
		System.out.println("POS: " + pos);
		PartsOfSpeech ps = PartsOfSpeech.valueOf(pos);
		if (ps != null)
			return ps.getStress();
		return -1;

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

		if (word.equals("'Cause"))
			return "because";

		return word;
	}

}
