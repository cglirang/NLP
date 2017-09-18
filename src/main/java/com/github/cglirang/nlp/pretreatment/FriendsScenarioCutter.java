
package com.github.cglirang.nlp.pretreatment;

import com.github.cglirang.nlp.pretreatment.bean.Lines;
import com.github.cglirang.nlp.utils.StringUtils;
import com.github.cglirang.nlp.utils.TextReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** Created by lirang2 on 2017/9/14. */
public class FriendsScenarioCutter {
	public static final String SCENARIO_INPUT_PATH = "pretreatment/Friends_Scenario.txt";
	public static final String CORPUS_OUPUT_PATH = "D:\\";

	private static final String END_LINES = "THE END";

	/** cut scenario into corpus
	 *
	 * @param inputPath
	 * @param outputPath */
	public void cutScenario (String inputPath, String outputPath, int windowSize) {
		// count
		int sceneNum = 0;
		int qaPair = 0;

		List<String> characters = new ArrayList<String>();
		List<Lines> linesList = new ArrayList<Lines>();

		TextReader textReader = null;
		BufferedWriter fw = null;

		String str = null;

		try {
			// read text
			textReader = new TextReader(inputPath);
			// write file
			fw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outputPath + "output_window_" + windowSize, true), "UTF-8"));

			// cut begin
			while ((str = textReader.readLine()) != null) {
				// "the end"
				if (this.isEnd(str)) {
					break;
				}
				// a new scene
				if (this.isNewScene(str)) {
					// remove scenes when character number less than 2
					if (2 < characters.size() && 0 != linesList.size()) {
						// generate tmp list
						List<Lines> tmpLinesList = this.generateTmpList(characters, linesList);
						// slide window
						List<String> corpus = this.windowSlides(tmpLinesList, windowSize);
						if (null != corpus && 0 != corpus.size()) {
							this.writeFile(fw, corpus);
							qaPair += corpus.size();
						}
						characters = new ArrayList<String>();
						linesList = new ArrayList<Lines>();
					}
					sceneNum += 1;
					continue;
				}
				// remove moves and descriptions or extract message from lines not in English
				str = this.removeBrackets(str);

				// save lines
				if (!StringUtils.nullOrEmpty(str)) {
					Lines lines = new Lines();
					String[] strings = str.split(":");

					if (!characters.contains(strings[0])) {
						characters.add(strings[0]);
					}
					lines.setWholeSentence(str);
					lines.setCharacter(strings[0].trim().toLowerCase());
					lines.setLines(strings[1]);
					linesList.add(lines);
				}
			}
			System.out.println("This scenario has " + sceneNum + " scenes, cut into " + qaPair + "QA pairs.");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(textReader != null ? textReader.getLineNumber() : 0 + "\t" + str);
		} finally {
			if (textReader != null) {
				try {
					textReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** whether it is the start of new scene
	 * 
	 * @return */
	private boolean isNewScene (String lines) {
		boolean newScene = false;

		char firstChar = lines.charAt(0);
		char lastChar = lines.charAt(lines.length() - 1);

		if (firstChar == '[' && lastChar == ']') {
			newScene = true;
		}

		return newScene;
	}

	/** whether the lines is "the end"
	 * 
	 * @param lines
	 * @return */
	private boolean isEnd (String lines) {
		boolean end = false;

		if (StringUtils.phraseEquals(lines, END_LINES)) {
			end = true;
		}

		return end;
	}

	/** remove brackets in lines
	 * 
	 * @param lines
	 * @return */
	private String removeBrackets (String lines) {
		// remove moves or extract message from lines not in English
		if (lines.contains("(") && lines.contains(")")) {
			lines = StringUtils.extractMessageOrRemove(lines, '(', ')', StringUtils.isEnglish(lines));
		}
		// remove description
		if (lines.contains("{") && lines.contains("}")) {
			lines = StringUtils.extractMessageOrRemove(lines, '{', '}', true);
		}

		return lines;
	}

	/** generate temporary lines list
	 * 
	 * @param characters
	 * @param linesList
	 * @return */
	private List<Lines> generateTmpList (List<String> characters, List<Lines> linesList) {
		// tmpList
		List<Lines> tmpList = new ArrayList<Lines>();

		int count = 0;
		String tmpCharacter = characters.get(0);

		// generate temporary lines list
		for (Lines lines : linesList) {
			// next speaker
			while (!StringUtils.phraseEquals(lines.getCharacter(), tmpCharacter)) {
				Lines tmpLines = new Lines();

				tmpLines.setCharacter(tmpCharacter);
				tmpLines.setLines("<idt>");
				tmpLines.setLines(tmpCharacter + ":<idt>");

				tmpList.add(tmpLines);

				count += 1;
				tmpCharacter = characters.get(count % characters.size());
			}
			tmpList.add(lines);
		}

		return tmpList;
	}

	/** slide window to get next corpus unit
	 * 
	 * @param tmpList
	 * @param windowSize
	 * @return */
	private List<String> windowSlides (List<Lines> tmpList, int windowSize) {
		List<String> corpus = new ArrayList<String>();

		// pointer
		int x = 0;
		int y = 0;

		StringBuffer stringBuffer = new StringBuffer();

		// slide window
		while (y < tmpList.size() + windowSize - 2) {
			if (y - x < windowSize) {
				corpus.add(stringBuffer.toString());

				x += 1;
				y = x;
			}
			stringBuffer.append(tmpList.get(y).getWholeSentence()).append("\n");

			y += 1;
		}

		return corpus;
	}

	/** write corpus into file
	 *
	 * @param fw
	 * @param corpus */
	private void writeFile (BufferedWriter fw, List<String> corpus) throws IOException {
		for (String str : corpus) {
			fw.write(str + "\n\n");
		}
		fw.write("\n");
		fw.flush();
	}

	public static void main (String[] args) {
		FriendsScenarioCutter friendsScenarioCutter = new FriendsScenarioCutter();
		friendsScenarioCutter.cutScenario(SCENARIO_INPUT_PATH, CORPUS_OUPUT_PATH, 3);

	}

}
