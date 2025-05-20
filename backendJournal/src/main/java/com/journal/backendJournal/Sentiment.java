package com.journal.backendJournal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Sentiment {
	LONELINESS("loneliness", Arrays.asList("lonely", "loneliness", "alone", "isolated",
			"isolation", "abandoned", "disconnected", "isolating")),
	ANGER("anger", Arrays.asList("frustrated", "annoyed", "angry", "furious",
			"irritated", "mad", "resentful", "resent", "resentment", "I want revenge")),
	ENVY("envy", Arrays.asList("envy", "jealous", "envious", "inferiority", "inferior", "jealousy")),
	ANXIETY("anxiety", Arrays.asList("anxious", "worried", "fearful", "restless", "stressed", 
			"nervous", "tense", "dread", "worry", "stress", "what if")),
	EMBARRASSMENT("embarrassment", Arrays.asList("humiliation", "embarrassed", "humiliated",
			"embarrassment", "mortified", "disrespected")),
	GUILT("guilt", Arrays.asList("regret", "fault", "shame", "ashamed", "shameful", "guilty",
			"guilt", "blame", "sinner")),
	DISAPPOINTMENT("disappointment", Arrays.asList("disappointment", "letdown", "failure",
			"dissapointed", "defeated")),
	LAZINESS("laziness", Arrays.asList("aimless", "lazy", "stuck", "bored", "complacent", "stagnant",
			"I don't care", "I really don't care")),///may add humility AND some extreme anger? hatred/revenge
	HOPELESSNESS("hopelessness", Arrays.asList("despair", "broken", "worthless", "depressed", "hopeless",
			"helpless")),
	INJURED("injured", Arrays.asList("injured", "injury", "healing", "miscarriage")),
	PRIDEFUL("prideful", Arrays.asList("injured", "injury", "healing", "miscarriage")),
	TIRED("tired", Arrays.asList("tired", "exhausted")),
	JUDGMENT("Judgmental", Arrays.asList("judgemental", "judgmental"));
	
	private final String baseWord;
	private final List<String> synonyms;

	Sentiment(String baseWord, List<String> synonyms) {
		this.baseWord = baseWord;
		this.synonyms = synonyms;
	}

	public String getBaseWord() {
		return baseWord;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public static List<String> getAllEmotions(){
		List<String> se = new ArrayList<>();
		for (Sentiment sentiment : Sentiment.values()) {
			se.addAll(sentiment.synonyms);
		}
		return se;
		
	}

	public static String findCategory(String synonym) {
		Optional<Sentiment> se =  Arrays.stream(Sentiment.values())
			.filter(sentiment -> sentiment.getBaseWord().equalsIgnoreCase(synonym)
					|| sentiment.getSynonyms().stream().anyMatch(s -> s.equalsIgnoreCase(synonym)))
			.findFirst();
		if(se.isPresent()) {
			return se.get().baseWord;
		}
		return null;
	}

	public static boolean contains(String synonym) {
		return Arrays.stream(Sentiment.values())
		.anyMatch(sentiment -> sentiment.getBaseWord().equalsIgnoreCase(synonym) 
				|| sentiment.getSynonyms().stream().anyMatch(s -> s.equalsIgnoreCase(synonym)));
	}
}
