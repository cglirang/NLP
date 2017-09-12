
package com.github.cglirang.nlp.keyword.algorithm;

import com.github.cglirang.nlp.Settings;
import com.github.cglirang.nlp.keyword.bean.rake.CandidateList;
import com.github.cglirang.nlp.keyword.bean.rake.SentenceTokenizer;
import com.github.cglirang.nlp.keyword.bean.rake.Sentences;
import com.github.cglirang.nlp.keyword.bean.rake.StopList;
import com.github.cglirang.nlp.utils.StringUtils;
import com.github.cglirang.nlp.utils.TextReader;

import java.util.*;

/** Created by lirang2 on 2017/8/24. */
public class Rake {

	private List<String> separateWords (final String text, final int minimumWordReturnSize) {

		final List<String> separateWords = new ArrayList<String>();
		final String[] words = text.split("[^a-zA-Z0-9_\\+\\-/]");

		if (words != null && words.length > 0) {

			for (final String word : words) {

				String wordLowerCase = word.trim().toLowerCase();

				if (wordLowerCase.length() > 0 && wordLowerCase.length() > minimumWordReturnSize
					&& !StringUtils.isNumber(wordLowerCase)) {

					separateWords.add(wordLowerCase);
				}
			}
		}

		return separateWords;
	}

	public Map<String, Double> calculateWordScores (List<String> phraseList) {

		final Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
		final Map<String, Integer> wordDegree = new HashMap<String, Integer>();
		final Map<String, Double> wordScore = new HashMap<String, Double>();

		for (final String phrase : phraseList) {

			final List<String> wordList = separateWords(phrase, 0);
			final int wordListLength = wordList.size();
			final int wordListDegree = wordListLength - 1;

			for (final String word : wordList) {

				if (!wordFrequency.containsKey(word)) {
					wordFrequency.put(word, 0);
				}

				if (!wordDegree.containsKey(word)) {
					wordDegree.put(word, 0);
				}

				wordFrequency.put(word, wordFrequency.get(word) + 1);
				wordDegree.put(word, wordDegree.get(word) + wordListDegree);
			}
		}

		final Iterator<String> wordIterator = wordFrequency.keySet().iterator();

		while (wordIterator.hasNext()) {
			final String word = wordIterator.next();

			wordDegree.put(word, wordDegree.get(word) + wordFrequency.get(word));

			if (!wordScore.containsKey(word)) {
				wordScore.put(word, 0.0);
			}

			wordScore.put(word, wordDegree.get(word) / (wordFrequency.get(word) * 1.0));
		}

		return wordScore;
	}

	public Map<String, Double> generateCandidateKeywordScores (List<String> phraseList, Map<String, Double> wordScore) {

		final Map<String, Double> keyWordCandidates = new HashMap<String, Double>();

		for (String phrase : phraseList) {

			final List<String> wordList = separateWords(phrase, 0);
			double candidateScore = 0;

			for (final String word : wordList) {
				candidateScore += wordScore.get(word);
			}

			keyWordCandidates.put(phrase, candidateScore);
		}

		return keyWordCandidates;
	}

	public static void main (String[] args) throws Exception {

		final String text = "hi I have been having a charging issue, and over heating issue, and a screen freezing issue with my phone, my phone company wont issue a warranty bcasue there is screen damage which I think it ironic since I have the droid turbo 2 , and the screen damage isn't causing any function issue to the phone , but chargers literally will stop working for my phone over night the phone wont turn on and sometimes when the phone is just sitting there (not being used) it will over heat, I actually almost dropped my phone  ;picking it up because it was so hot (not even being plugged in charging )";

		final Rake rakeInstance = new Rake();

		final Sentences sentences = new SentenceTokenizer().split(text);
		final StopList stopList = new StopList()
			.generateStopWords(new TextReader(Settings.CONFIG_PATH + "stopword/SmartStoplist.txt"));
		final CandidateList candidateList = new CandidateList().generateKeywords(sentences, stopList.getStopWords());

		final Map<String, Double> wordScore = rakeInstance.calculateWordScores(candidateList.getPhraseList());
		final Map<String, Double> keywordCandidates = rakeInstance.generateCandidateKeywordScores(candidateList.getPhraseList(),
			wordScore);

		System.out.println("keyWordCandidates = " + keywordCandidates);

		System.out.println("sortedKeyWordCandidates = " + rakeInstance.sortKeyWordCandidates(keywordCandidates));
	}

	public LinkedHashMap<String, Double> sortKeyWordCandidates (Map<String, Double> keywordCandidates) {

		final LinkedHashMap<String, Double> sortedKeyWordCandidates = new LinkedHashMap<String, Double>();
		int totaKeyWordCandidates = keywordCandidates.size();
		final List<Map.Entry<String, Double>> keyWordCandidatesAsList = new LinkedList<Map.Entry<String, Double>>(
			keywordCandidates.entrySet());

		Collections.sort(keyWordCandidatesAsList, new Comparator() {
			@Override
			public int compare (Object o1, Object o2) {
				return ((Map.Entry<String, Double>)o2).getValue().compareTo(((Map.Entry<String, Double>)o1).getValue());
			}
		});

		totaKeyWordCandidates = totaKeyWordCandidates / 3;
		for (final Map.Entry<String, Double> entry : keyWordCandidatesAsList) {
			sortedKeyWordCandidates.put(entry.getKey(), entry.getValue());
			if (--totaKeyWordCandidates == 0) {
				break;
			}
		}

		return sortedKeyWordCandidates;
	}

}
