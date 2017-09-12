
package com.github.cglirang.nlp.keyword.bean.rake;

/** Created by lirang2 on 2017/8/24. */
public class Word {

	private String value;

	public Word (String value) {
		this.value = value;
	}

	public boolean isEmpty () {
		return value == null || value.trim().isEmpty();
	}

	public String getAsLowerCase () {
		return value.trim().toLowerCase();
	}
}
