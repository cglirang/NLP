
package com.github.cglirang.nlp.utils;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/** Created by lirang2 on 2017/9/12. */
public class StanfordNLP {
	// pipeline
	public static StanfordCoreNLP pipeline = null;

	/** init standford corenlp */
	public static void initStanfordPipeline () {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
		// Add in sentiment
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");

		pipeline = new StanfordCoreNLP(props);

	}

}
