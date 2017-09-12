
package com.github.cglirang.nlp.keyword.bean.rake;

import com.github.cglirang.nlp.utils.TextReader;

import java.io.IOException;

/** Created by lirang2 on 2017/8/24. */
public class StopList {

	private StopWords stopWords;

	public StopList () {
		this.stopWords = new StopWords();
	}

	public StopList generateStopWords (TextReader textReader) throws IOException {
		// add to set
		String line = null;
		try {
			while ((line = textReader.readLine()) != null) {
				stopWords.add(line);
			}
			// close stream
			textReader.close();
		} catch (Exception e) {
			System.out.println("init stopWords error!");
			e.printStackTrace();
		}

		return this;
	}

	public StopWords getStopWords () {
		return stopWords;
	}
}
