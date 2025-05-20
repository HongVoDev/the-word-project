package com.journal.backendJournal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EmotionResult;
import com.ibm.watson.natural_language_understanding.v1.model.Features;

@Service
public class EmotionAnalysisService {
	// Main 5 emotions
	public static final String SADNESS = "Sadness";
	public static final String FEAR = "Fear";
	public static final String ANGER = "Anger";
	public static final String DISGUST = "Disgust";
	public static final String JOY = "Joy";
	// Results
	public static final String FEAR_ANXIETY = "Fear";
	public static final String SADNESS_HOPELESS_LONELINESS = "Sadness";
	public static final String SHOCK_OVERWHELMED = "Shock";
	public static final String ANGER_RESENTMENT = "Anger";
	public static final String GUILT = "Guilt";
	public static final String ENVY = "Envy";
	// Constants
	private static final double DOMINANT_THRESHOLD = 0.38; // Minimum score for an emotion to be considered dominant
	private static final double SECONDARY_THRESHOLD = 0.25; // Minimum score for a secondary emotion to be significant
	private static final double SIGNIFICANT_DIFFERENCE = 0.16; // Score difference between primary and secondary for

	@Autowired
	private WatsonConfig watsonConfig;

	public String analyzeEmotion(String text) {
		String initialEmotion = initalFindEmotion(text);
		if (initialEmotion != null) {
			return initialEmotion;
		}
		IamAuthenticator authenticator = new IamAuthenticator.Builder().apikey(watsonConfig.getApikey()).build();
		NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding(
				watsonConfig.getVersion(), authenticator);
		naturalLanguageUnderstanding.setServiceUrl(watsonConfig.getUrl());

		Features features = new Features.Builder().emotion(new EmotionOptions.Builder().document(true).build()).build();

		AnalyzeOptions parameters = null;
		AnalysisResults response = new AnalysisResults();
		EmotionResult emotions = new EmotionResult();
		
		try {
			// Extract the emotions
			if (text.length() < 10) {
				text = "Today I am feeling " + text;
			}
			parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
			response = naturalLanguageUnderstanding.analyze(parameters).execute().getResult();
			emotions = response.getEmotion();
		}
		catch (Exception e) {
			text = "Today I am feeling " + text;
			parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
			response = naturalLanguageUnderstanding.analyze(parameters).execute().getResult();
			emotions = response.getEmotion();
		}
		// Map to a more specific emotion
		return mapToSpecificEmotion(emotions);
	}

	private String mapToSpecificEmotion(EmotionResult emotions) {
		double anger = emotions.getDocument().getEmotion().getAnger();
		double disgust = emotions.getDocument().getEmotion().getDisgust();
		double fear = emotions.getDocument().getEmotion().getFear();
		double joy = emotions.getDocument().getEmotion().getJoy();
		double sadness = emotions.getDocument().getEmotion().getSadness();

		System.out.println(" Joy = " + joy + ", " +"Anger = " + anger + ", " +"Fear = "
		 + fear + ", " + "Sadness = " + sadness + ", " + "Disgust = " + disgust);
		
		return classifyEmotion(joy, anger, fear, sadness, disgust);
	}

	public String classifyEmotion(double joy, double anger, double fear, double sadness, double disgust) {
		String dominantEmotion = findDominantEmotion(joy, anger, fear, sadness, disgust);

		if (joy > 0.8) {
			return JOY;
		}
		if(fear > 0.4) {
			return FEAR;
		}
		if (sadness > 0.4) {
			return SADNESS_HOPELESS_LONELINESS;
		}
		if (anger > 0.365) {
			return ANGER;
		}
		// Anger/resentment
		if (dominantEmotion.equals(EmotionAnalysisService.ANGER) && sadness > SECONDARY_THRESHOLD) {
			return EmotionAnalysisService.ANGER_RESENTMENT;
		}
		if (dominantEmotion.equals(EmotionAnalysisService.ANGER) && disgust > SECONDARY_THRESHOLD) {
			return EmotionAnalysisService.ANGER_RESENTMENT; // Sometimes disgust entries are actually anger.
		}
		if (sadness > DOMINANT_THRESHOLD && anger > SECONDARY_THRESHOLD && (sadness > fear)) {
			return EmotionAnalysisService.ANGER_RESENTMENT;
		}
		if (dominantEmotion.equals(EmotionAnalysisService.ANGER) && anger > DOMINANT_THRESHOLD) {
			return EmotionAnalysisService.ANGER_RESENTMENT;
		}
		// Shock/overwhelmed
		if ((dominantEmotion.equals(EmotionAnalysisService.FEAR)
				|| dominantEmotion.equals(EmotionAnalysisService.SADNESS)) && fear > DOMINANT_THRESHOLD
				&& sadness > DOMINANT_THRESHOLD) {
			return EmotionAnalysisService.SHOCK_OVERWHELMED;
		}

		if ((fear > SECONDARY_THRESHOLD && sadness > SECONDARY_THRESHOLD) && (joy < SECONDARY_THRESHOLD)) {
			if ((fear + sadness) > DOMINANT_THRESHOLD) {
				return EmotionAnalysisService.SHOCK_OVERWHELMED;
			}
		}
		if (joy < SECONDARY_THRESHOLD
				&& (anger > SECONDARY_THRESHOLD && fear > SECONDARY_THRESHOLD && sadness > SECONDARY_THRESHOLD)) {
			return EmotionAnalysisService.SHOCK_OVERWHELMED;
		}

		// Sadness/hopeless/loneliness
		if (dominantEmotion.equals(EmotionAnalysisService.SADNESS) && fear > SECONDARY_THRESHOLD) {
			return EmotionAnalysisService.SADNESS_HOPELESS_LONELINESS;
		}

		if (sadness > DOMINANT_THRESHOLD
				&& (anger < SECONDARY_THRESHOLD && fear < SECONDARY_THRESHOLD && disgust < SECONDARY_THRESHOLD)) {
			return EmotionAnalysisService.SADNESS_HOPELESS_LONELINESS;
		}

		if (dominantEmotion.equals(EmotionAnalysisService.SADNESS) && sadness > DOMINANT_THRESHOLD) {
			return EmotionAnalysisService.SADNESS_HOPELESS_LONELINESS;
		}

		// Fear/anxiety
		if (dominantEmotion.equals(EmotionAnalysisService.FEAR)
				&& (anger > SECONDARY_THRESHOLD || sadness > SECONDARY_THRESHOLD)) {
			return EmotionAnalysisService.FEAR_ANXIETY;
		}
		if (dominantEmotion.equals(EmotionAnalysisService.FEAR) && fear > DOMINANT_THRESHOLD) {
			return EmotionAnalysisService.FEAR_ANXIETY;
		}
		if (fear > DOMINANT_THRESHOLD && (anger < SECONDARY_THRESHOLD && sadness < SECONDARY_THRESHOLD)) {
			return EmotionAnalysisService.FEAR_ANXIETY;
		}

		// Guilt
		if (dominantEmotion.equals(EmotionAnalysisService.SADNESS) && disgust > SECONDARY_THRESHOLD) {
			return EmotionAnalysisService.GUILT; // Because disgust often shows up in guilt prompts.
		}
		if ((sadness > SECONDARY_THRESHOLD && disgust > SECONDARY_THRESHOLD) && (joy < SECONDARY_THRESHOLD)) {
			return EmotionAnalysisService.GUILT;
		}
		if ((disgust > SECONDARY_THRESHOLD && anger > SECONDARY_THRESHOLD) && (joy < SECONDARY_THRESHOLD)) {
			return EmotionAnalysisService.GUILT;
		}

		// Joy
		if (dominantEmotion.equals(EmotionAnalysisService.JOY) && joy > DOMINANT_THRESHOLD) {
			return EmotionAnalysisService.JOY;
		}

		// Disgust
		if (dominantEmotion.equals(EmotionAnalysisService.DISGUST) && disgust > DOMINANT_THRESHOLD) {
			return EmotionAnalysisService.DISGUST; // May need to refine this more
		}

		// If no specific conditions are met, return combination of dominant emotion and
		// secondary if it meets the threshold.
		else {
			String secondDominantEmotion = findSecondMostDominantEmotion(joy, anger, fear, sadness, disgust);

			if (secondDominantEmotion != null) {
				return finalCase(dominantEmotion, secondDominantEmotion, joy, anger, fear, sadness, disgust);  
			}
			return dominantEmotion;
		}
	}

	private String findDominantEmotion(double joy, double anger, double fear, double sadness, double disgust) {
		// Finds the emotion with the highest score.
		double maxScore = Math.max(Math.max(Math.max(joy, anger), fear), Math.max(sadness, disgust));

		if (joy == maxScore)
			return EmotionAnalysisService.JOY;
		if (anger == maxScore)
			return EmotionAnalysisService.ANGER;
		if (fear == maxScore)
			return EmotionAnalysisService.FEAR;
		if (sadness == maxScore)
			return EmotionAnalysisService.SADNESS;
		return EmotionAnalysisService.DISGUST;
	}

	private String findSecondMostDominantEmotion(double joy, double anger, double fear, double sadness,
			double disgust) {
		Map<String, Double> emotionScores = new HashMap<String, Double>();
		emotionScores.put(EmotionAnalysisService.JOY, joy);
		emotionScores.put(EmotionAnalysisService.ANGER, anger);
		emotionScores.put(EmotionAnalysisService.FEAR, fear);
		emotionScores.put(EmotionAnalysisService.SADNESS, sadness);
		emotionScores.put(EmotionAnalysisService.DISGUST, disgust);
		// Sort the emotions based on their scores in descending order.

		List<Entry<String, Double>> sortedEmotions = new ArrayList<>(emotionScores.entrySet());
		sortedEmotions.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

		if (sortedEmotions.size() < 2) {
			return null;
		}

		return sortedEmotions.get(1).getKey();
	}

	private Boolean finalCompare(double firstEmotion, double secondEmotion) {
		if (Math.abs(secondEmotion - secondEmotion) <= SIGNIFICANT_DIFFERENCE) {
			return true;
		}
		return false;
	}

	private String finalCase(String dominantEmotion, String secondDominantEmotion, double joy, double anger,
			double fear, double sadness, double disgust) {
		if ((dominantEmotion.equals(EmotionAnalysisService.DISGUST)
				&& secondDominantEmotion.equals(EmotionAnalysisService.JOY))
				|| (dominantEmotion.equals(EmotionAnalysisService.JOY)
						&& secondDominantEmotion.equals(EmotionAnalysisService.DISGUST))) {
			if (finalCompare(disgust, joy)) {
				return EmotionAnalysisService.GUILT;
			}
		} else if ((dominantEmotion.equals(FEAR) && secondDominantEmotion.equals(SADNESS))
				|| (dominantEmotion.equals(SADNESS) && secondDominantEmotion.equals(FEAR))) {
			if (finalCompare(fear, sadness)) {
				return SADNESS_HOPELESS_LONELINESS;
			}
		} else if ((dominantEmotion.equals(EmotionAnalysisService.DISGUST) && secondDominantEmotion.equals(SADNESS))
				|| (dominantEmotion.equals(SADNESS) && secondDominantEmotion.equals(EmotionAnalysisService.DISGUST))) {
			if (finalCompare(disgust, sadness)) {
				return SHOCK_OVERWHELMED;
			}
		} else if ((dominantEmotion.equals(ANGER) && secondDominantEmotion.equals(FEAR))
				|| (dominantEmotion.equals(FEAR) && secondDominantEmotion.equals(ANGER))) {
			if (finalCompare(fear, anger)) {
				return SHOCK_OVERWHELMED;
			}
		} else if ((dominantEmotion.equals(EmotionAnalysisService.SADNESS)
				&& secondDominantEmotion.equals(EmotionAnalysisService.JOY))
				|| (dominantEmotion.equals(EmotionAnalysisService.JOY)
						&& secondDominantEmotion.equals(EmotionAnalysisService.SADNESS))) {
			if (finalCompare(sadness, joy)) {
				return SHOCK_OVERWHELMED;
			}
		} else if ((dominantEmotion.equals(ANGER) && secondDominantEmotion.equals(DISGUST))
				|| (dominantEmotion.equals(DISGUST) && secondDominantEmotion.equals(ANGER))) {
			if (finalCompare(disgust, anger)) {
				return SHOCK_OVERWHELMED;
			}
		} else if ((dominantEmotion.equals(EmotionAnalysisService.ANGER)
				&& secondDominantEmotion.equals(EmotionAnalysisService.JOY))
				|| (dominantEmotion.equals(EmotionAnalysisService.JOY)
						&& secondDominantEmotion.equals(EmotionAnalysisService.ANGER))) {
			if (finalCompare(anger, joy)) {
				return ANGER_RESENTMENT;
			}
		}
		return "Unclear";
	}
	
	private String findFirstContainedWord(String text, List words) {
			if (text == null || text.isEmpty() || words == null || words.isEmpty()) {
				return null;
			}
			StringBuilder patternBuilder = new StringBuilder();
			patternBuilder.append("(?i)\\b(");
	
			for (int i = 0; i < words.size(); i++) {
				if (i > 0) {
					patternBuilder.append("|");
				}
				patternBuilder.append(Pattern.quote((String) words.get(i)));
			}
			patternBuilder.append(")\\b");
	
			Pattern pattern = Pattern.compile(patternBuilder.toString());
			java.util.regex.Matcher matcher = pattern.matcher(text);
	
			if (matcher.find()) {
				return matcher.group(1);
		}
			return null;
	}
	
	private String initalFindEmotion(String text) {
		String emotion = findFirstContainedWord(text, Sentiment.getAllEmotions());
		if (emotion != null) {
			return Sentiment.findCategory(emotion);
		}
		return null;
	}

}
