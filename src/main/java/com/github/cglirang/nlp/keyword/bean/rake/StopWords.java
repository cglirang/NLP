
package com.github.cglirang.nlp.keyword.bean.rake;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/** Created by lirang2 on 2017/8/24. */
public class StopWords {

	private List<String> words;

	public StopWords () {
		this.words = new ArrayList<String>();
	}

	public void add (String line) {
		if (!line.startsWith("#")) { // add the line which is not a comment
			this.words.add(line);
		}
	}

	public Pattern getStopWordsPattern () {
		final StringBuilder stopWordsPatternBuilder = new StringBuilder();
		for (final String stopWord : words) {
			stopWordsPatternBuilder.append("\\b");
			stopWordsPatternBuilder.append(stopWord);
			stopWordsPatternBuilder.append("\\b");
			stopWordsPatternBuilder.append("|");
		}
		String stopWordsPatternString = stopWordsPatternBuilder.toString();
		String stopWordPatternString = StringUtils.chop(stopWordsPatternString); // remove last "|"
		return Pattern.compile(stopWordPatternString, Pattern.CASE_INSENSITIVE);
	}
}
