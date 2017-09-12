
package com.github.cglirang.nlp.keyword.bean.rake;

import java.util.Iterator;

/** Created by lirang2 on 2017/8/24. */
public class Sentences implements Iterator<Sentence> {

	private Sentence[] sentences;
	private int currentPos;

	public Sentences () {
	}

	public Sentences addSentences (String[] values) {
		if (values != null) {
			this.sentences = new Sentence[values.length];
			int count = 0;
			for (String value : values) {
				this.sentences[count++] = new Sentence(value);
			}
		}
		return this;
	}

	public Sentences iterator () {
		currentPos = 0;
		return this;
	}

	@Override
	public boolean hasNext () {
		return currentPos < sentences.length;
	}

	@Override
	public Sentence next () {
		Sentence current = sentences[currentPos];
		currentPos++;
		return current;
	}
}
