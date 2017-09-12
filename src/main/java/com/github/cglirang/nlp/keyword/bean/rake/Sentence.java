
package com.github.cglirang.nlp.keyword.bean.rake;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Created by lirang2 on 2017/8/24. */
public class Sentence {

	private String value;

	public Sentence (String value) {
		this.value = value;
	}

	public Phrase generatePhrasesFrom (Pattern stopWordPattern) {
		Matcher matcher = stopWordPattern.matcher(value);
		String sentenceWithoutStopWords = matcher.replaceAll("|");
		return new Phrase().addWords(sentenceWithoutStopWords.split("\\|"));
	}

}
