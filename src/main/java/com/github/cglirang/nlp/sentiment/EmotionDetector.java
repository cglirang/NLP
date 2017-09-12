
package com.github.cglirang.nlp.sentiment;

import com.github.cglirang.nlp.Settings;
import com.github.cglirang.nlp.utils.DataConversionUtils;
import com.github.cglirang.nlp.utils.StanfordNLP;
import com.github.cglirang.nlp.utils.TextReader;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmotionDetector {

	// complain threshold
	private static double THRESHOLD = 0.285;
	// curse dic
	private static Set<String> curseDic = new HashSet<String>();
	// corenlp pipeline
	private static StanfordCoreNLP pipeline;

	/** EmotionDetector initialization */
	public void init () {
		this.initCorenlp();
		this.initCurseDic(Settings.CONFIG_PATH + "sentiment/curseDic.txt");
	}

	/** init curse dic
	 *
	 * @param path */
	private void initCurseDic (String path) {
		TextReader textReader = null;
		try {
			// read text
			textReader = new TextReader(path);

			// add to set
			String line = null;
			while ((line = textReader.readLine()) != null) {
				curseDic.add(line);
			}

		} catch (Exception e) {
			System.out.println("init curse dic error!");
			e.printStackTrace();
		} finally {
			if (textReader != null) {
				try {
					textReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/** init stanford corenlp */
	private void initCorenlp () {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
		StanfordNLP.initStanfordPipeline();

		pipeline = StanfordNLP.pipeline;

	}

	// @Override
	public boolean isComplain (String s) {
		// Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
		Annotation annotation;
		annotation = new Annotation(s);

		// run all the selected Annotators on this text
		pipeline.annotate(annotation);

		// get sentence
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

		// sentence sentiment score
		double score = 0;
		if (sentences != null && !sentences.isEmpty()) {
			for (CoreMap sentence : sentences) {
				score += this.getSentimentScore(sentence);
			}
			score = score / sentences.size();

		}
		System.out.println("final score: " + score);

		return aboveThreshold(score);
	}

	// @Override
	public double emotionScore (String s) {
		// Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
		Annotation annotation;
		annotation = new Annotation(s);

		// run all the selected Annotators on this text
		pipeline.annotate(annotation);

		// get sentence
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

		// sentence sentiment score
		double score = 0;
		if (sentences != null && !sentences.isEmpty()) {
			for (CoreMap sentence : sentences) {
				score += this.getSentimentScore(sentence);
			}
			score = score / sentences.size();

		}
		System.out.println("final score: " + score);

		return score;
	}

	/** get sentiment score, integrate the results of Stanford and curse dic matching
	 *
	 * @param sentence
	 * @return */
	private double getSentimentScore (CoreMap sentence) {
		// System.out.println("The sentence is: " + sentence.toString() + "\nOverall sentiment rating is: "
		// + sentence.get(SentimentCoreAnnotations.SentimentClass.class));

		// get distribution value of CoreNLP Sentiment
		Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
		SimpleMatrix sm = RNNCoreAnnotations.getPredictions(tree);
		System.out.println("distribution value of CoreNLP Sentiment: " + sm);

		// generate curse dic vector
		double[] data = {9, 5, 5, 2, 1};

		// find curse word
		int countCurseWord = 0;
		List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
		for (CoreMap token : tokens) {
			String str = token.toString().split("-")[0];

			// Increase the proportion of negative
			if (curseDic.contains(str)) {
				System.out.println("find curse word: " + str);
				data[0] = data[0] + 2;
				data[1] = data[1] + 2;
				countCurseWord += 1;
			}
		}
		double[] normalizedData = DataConversionUtils.sumEqualsOneNormalize(data);

		// curse dic weights matrix
		SimpleMatrix matrix = new SimpleMatrix(1, 5, false, normalizedData);
		// System.out.println("curse dic weights matrix: " + matrix);

		// final score
		double score = matrix.dot(sm) * (1 + (double)countCurseWord / (double)tokens.size());
		System.out.println("sentiment score is " + score);

		return score;
	}

	private boolean aboveThreshold (double score) {
		if (score >= THRESHOLD) {
			System.out.println("sentiment score is greater or equal to the threshold " + THRESHOLD);
			return true;
		} else {
			System.out.println("sentiment score is less than the threshold " + THRESHOLD);
			return false;
		}
	}

	public static void main (String[] args) {
		String text = "fuck you!";

		EmotionDetector emotionDetector = new EmotionDetector();
		emotionDetector.init();
		emotionDetector.isComplain(text);

	}
}
