
package com.github.cglirang.nlp.pretreatment;

import com.github.cglirang.nlp.utils.StanfordNLP;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DependencyParser {

	public void runAllAnnotators (String text) {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
		StanfordNLP.initStanfordPipeline();
		StanfordCoreNLP pipeline = StanfordNLP.pipeline;

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		parserOutput(document);
	}

	public void parserOutput (Annotation document) {
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			System.out.println("语法树：");
			System.out.println(tree.toString());

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.AlternativeDependenciesAnnotation.class);
			System.out.println("依存句法：");

			PrintWriter pw = new PrintWriter(System.out);
			pw.print(this.toList(dependencies));
			pw.println();

			pw.flush();
		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
	}

	private String toList (SemanticGraph dependencies) {
		StringBuilder buf = new StringBuilder();
		Iterator var2 = dependencies.getRoots().iterator();

		while (var2.hasNext()) {
			IndexedWord root = (IndexedWord)var2.next();
			buf.append("root(ROOT-0, ");
			buf.append(root.toString(edu.stanford.nlp.ling.CoreLabel.OutputFormat.VALUE_INDEX)).append(")\n");
		}

		var2 = dependencies.edgeListSorted().iterator();

		while (var2.hasNext()) {
			SemanticGraphEdge edge = (SemanticGraphEdge)var2.next();
			buf.append(edge.getRelation().toString()).append("(");
			buf.append(edge.getSource().toString(edu.stanford.nlp.ling.CoreLabel.OutputFormat.VALUE_INDEX)).append(",");
			buf.append(edge.getTarget().toString(edu.stanford.nlp.ling.CoreLabel.OutputFormat.VALUE_INDEX)).append(")\n");
		}

		return buf.toString();
	}

	public static void main (String[] args) {
		DependencyParser dependencyParser = new DependencyParser();

		// read some text in the text variable
		String text = "why spend $9 on the same stuff you can get for a buck or so in that greasy little vidgame pit in the theater lobby ?"; // Add
// String text = "interesting , but not compelling . "; // Add your text here!

		dependencyParser.runAllAnnotators(text);
	}
}
