
package com.github.cglirang.nlp.keyword.bean.rake;

/** Created by lirang2 on 2017/8/24. */
public class SentenceTokenizer {

	private String grammar;

	public SentenceTokenizer () {
		this.grammar = "[.!?,;:\\t\\\\-\\\\\"\\\\(\\\\)\\\\\\'\\u2019\\u2013]";
	}

	public SentenceTokenizer (String grammar) {
		this.grammar = grammar;
	}

	public Sentences split (String input) {
		return new Sentences().addSentences(input.split(grammar));
	}
}
