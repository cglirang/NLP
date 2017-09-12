
package com.github.cglirang.nlp.keyword.bean;

import java.io.Serializable;

/** Created by gefujiang on 2017/8/22. */

public class Keyword implements Comparable<Keyword>,Serializable{
	public Keyword () {
	}

	public Keyword (String wd, double sc) {
		word = wd;
		score = sc;
	}

	public String word;// the word
	public double score;// the score

	/** if use sort, it will be desc */
	public int compareTo (Keyword ob) {
		Double f = new Double(ob.score);
		return f.compareTo(score);
	}

	public String getWord () {
		return word;
	}

	public double getScore () {
		return score;
	}

	@Override
	public String toString() {
		return "Keyword{" +
				"word='" + word + '\'' +
				", score=" + score +
				'}';
	}
}
