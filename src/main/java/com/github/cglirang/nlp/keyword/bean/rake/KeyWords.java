
package com.github.cglirang.nlp.keyword.bean.rake;

import java.util.ArrayList;
import java.util.List;

/** Created by lirang2 on 2017/8/24. */
public class KeyWords {

	private List<String> value;

	public KeyWords () {
		this.value = new ArrayList<String>();
	}

	public void add (String word) {
		value.add(word);
	}

	public List<String> getKeyWords () {
		return value;
	}
}
