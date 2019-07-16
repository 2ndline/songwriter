package com.secondline.songwriter.model;

public enum PartsOfSpeech {
	// for single-syllable words:

	// nouns, verbs, adjectives, adverbs are stressed
	// prepositions, articles, conj, aux verbs indicating tense/mood,
	// personal pronouns, relative pronouns are not

	cc("Coordinating conjunction", 0), cd("Cardinal number", 1), dt(
			"Determiner", 0), ex("Existential there", 0), fw("Foreign word", 0), in(
			"Preposition or subordinating conjunction", 1), jj("Adjective", 1), jjr(
			"Adjective, comparative", 1), jjs("Adjective, superlative", 1), ls(
			"List item marker", 0), md("Modal", 0), nn(
			"Noun, singular or mass", 1), nns("Noun, plural", 1), nnp(
			"Proper noun, singular", 1), nnps("Proper noun, plural", 1), pdt(
			"Predeterminer", 0), pos("Possessive ending", 0), prp(
			"Personal pronoun", 0), prp$("Possessive pronoun", 0), rb("Adverb",
			1), rbr("Adverb, comparative", 1), rbs("Adverb, superlative", 1), rp(
			"Particle", 0), sym("Symbol", 0), to("to", 0), uh("Interjection", 0), vb(
			"Verb, base form", 1), vbd("Verb, past tense", 1), vbg(
			"Verb, gerund or present participle", 1), vbn(
			"Verb, past participle", 1), vbp(
			"Verb, non-3rd person singular present", 1), vbz(
			"Verb, 3rd person singular present", 1), wdt("Wh-determiner", 0), wp(
			"Wh-pronoun", 0), wp$("Possessive wh-pronoun", 0), wrb("Wh-adverb",
			1);

	private int stress;

	PartsOfSpeech(String desc, int stress) {
		this.stress = stress;
	}

	public int getStress() {
		return stress;
	}

}