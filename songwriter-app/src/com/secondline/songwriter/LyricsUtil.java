package com.secondline.songwriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rita.RiLexicon;
import rita.RiString;

import com.secondline.songwriter.model.Lyric;

public class LyricsUtil {

	private static RiLexicon lexicon = new RiLexicon();

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
		System.out.println("Array len "+arrayLen);
		List<Integer> stressList = new ArrayList<Integer>();
		for (int i = 0; i < wordValues.length; ++i) {
			String word = wordValues[i];
			String fullWord = getFullWord(word);
			
			if(fullWord != word){
				System.out.println("Shortened word: " + fullWord);
				stressList.add(getStressMonoSyllable(fullWord));
				System.out.println("Calculated stress: " + stressList.get(stressList.size() - 1));
			} else if (stressValues[i].contains("/") ) {
				// multi-syllabic
				System.out.println("Polysyllabic word: " + fullWord);
				String[] wordStress = stressValues[i].split("/");
				for (int j = 0; j < wordStress.length; ++j) {
					stressList.add(Integer.parseInt(wordStress[j]));
					System.out.println("Calculated stress: " + stressList.get(stressList.size() - 1));
				}
			} else {
				System.out.println("Monosyllabic word: " + word);
				stressList.add(getStressMonoSyllable(word));
				System.out.println("Calculated stress: " + stressList.get(stressList.size() - 1));
			}
		}
		
		Integer[] result = new Integer[stressList.size()];
		result = stressList.toArray(result);
		System.out.println("Returning: "+Arrays.toString(result));
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
