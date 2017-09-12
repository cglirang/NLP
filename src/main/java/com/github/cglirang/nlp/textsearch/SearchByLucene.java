
package com.github.cglirang.nlp.textsearch;

import com.github.cglirang.nlp.Settings;
import com.github.cglirang.nlp.utils.TextReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Full-Text search by Lucene
 * 
 * Created by lirang2 on 2017/9/7. */
public class SearchByLucene {
	// Store the index in memory:
	private static Directory directory = null;
	private static Analyzer analyzer = null;

	private static final String PATH = "question.txt";

	private static final String QUESTION_NAME = "question_name";
	private static final String ID = "id";

	/** construction */
	public SearchByLucene () {
		if (null == directory || null == analyzer) {
			directory = new RAMDirectory();
			analyzer = new StandardAnalyzer();

			this.createIndex(Settings.CONFIG_PATH + "textsearch/" + PATH);
		}
	}

	/** create index
	 *
	 * @param path
	 * @return */
	public boolean createIndex (String path) {
		List<String> stringList = this.getStringList(path);

		IndexWriter iwriter = null;

		try {
			// create index writer
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			iwriter = new IndexWriter(directory, config);

			// write documents
			for (String text : stringList) {
				Document doc = new Document();
				String[] arr = text.split("\t");
				doc.add(new TextField(ID, arr[0], Field.Store.YES));
				doc.add(new TextField(QUESTION_NAME, arr[1], Field.Store.YES));
				iwriter.addDocument(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("create index error!");
		} finally {
			try {
				assert iwriter != null;
				iwriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("iwriter close error!");
			}
		}

		return true;
	}

	/** get string list of file
	 *
	 * @param path
	 * @return */
	private List<String> getStringList (String path) {
		List<String> stringList = new ArrayList<String>();

		TextReader textReader = null;

		try {
			textReader = new TextReader(path);
			// add to set
			String line = null;
			while ((line = textReader.readLine()) != null) {
				stringList.add(line);
			}
		} catch (Exception e) {
			System.out.println("read text error!");
			e.printStackTrace();
		} finally {
			try {
				// close stream
				textReader.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("textReader close error!");
			}
		}
		return stringList;
	}

	/** search index
	 *
	 * @param keywords */
	public List<String> searchIndex (List<String> keywords, boolean allField, int n) {
		List<String> questions = new ArrayList<String>();

		DirectoryReader ireader = null;
		IndexSearcher isearcher = null;

		try {
			ireader = DirectoryReader.open(directory);
			isearcher = new IndexSearcher(ireader);

			// Parse query that searches for keywords:
			QueryParser queryParser = new QueryParser(QUESTION_NAME, analyzer);
			BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
			for (String keyword : keywords) {
				Query query = queryParser.parse(keyword);
				BooleanClause booleanClause = new BooleanClause(query, BooleanClause.Occur.SHOULD);
				booleanQuery.add(booleanClause);

				if (allField) {
				}
			}

			ScoreDoc[] hits = isearcher.search(booleanQuery.build(), n).scoreDocs;

			// Iterate through the results:
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				questions.add(hitDoc.get(ID));
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("search index error!");
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("parse error!");
		} finally {
			try {
				assert ireader != null;
				ireader.close();
			} catch (IOException e) {
				System.out.println("stream close error!");
				e.printStackTrace();
			}
		}

		return questions;
	}

	public static void main (String[] args) {
		SearchByLucene searchByLucene = new SearchByLucene();
		searchByLucene.createIndex(Settings.CONFIG_PATH + "textsearch/" + PATH);

		List<String> keywords = new ArrayList<String>();
		keywords.add("phone hands-free");

		List<String> questions = searchByLucene.searchIndex(keywords, false, 5);

		System.out.println("\nQuestions: ");
		for (String question : questions) {
			System.out.println(question + "\n");
		}

	}

}
