
package com.github.cglirang.nlp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Created by gefujiang on 2017/6/25. */
public class StringUtils {
	public static void main (String[] args) throws Exception {

	}

	/*** 返回字符串s中第一个空白字符的位置 */
	public static int indexOfEmpty (String s) {
		return indexOfEmpty(s, 0);
	}

	public static int indexOfEmpty (String s, int start) {
		if (s == null) return -1;

		int i = start;
		while (i < s.length() && (!Character.isWhitespace(s.charAt(i))))
			i++;
		if (i == s.length()) return -1;
		return i;
	}

	/** prefix of s1 and s2 by word */
	public static String prefixByWord (String s1, String s2) {
		if (s1 == null || s2 == null) return null;
		if (s1.length() == 0 || s2.length() == 0) return "";

		String separator = "\\s+";// space
		String[] its1 = s1.split(separator);
		String[] its2 = s1.split(separator);
		if (its1.length == 0 || its2.length == 0) return "";
		int len = Math.min(its1.length, its2.length);
		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < len; i++) {
			if (its1[i].compareTo(its2[i]) != 0) break;
			prefix.append(its1[i] + " ");
		}
		return prefix.toString().trim();
	}

	public static boolean nullOrEmpty (String s) {
		if (s == null || s.length() == 0) return true;
		return false;
	}

	public static boolean regexMatch (String s, String regex) {
		Pattern pat = Pattern.compile(regex);
		Matcher m = pat.matcher(s);
		return m.find();
	}

	public static boolean isRegex (String s) {
		if (s == null || s.length() == 0) return false;
		if (s.split("[|()+*?{}\\\\\\^\\[\\]]").length > 1) // |()+*?{}\^[]
			return true;
		return false;
	}

	/** 去掉注释：“#”或“//”之后的是注释 */
	public static String clearComments (String li) {
		if (li == null) return null;
		if (li.length() == 0) return li;
		int notepos1 = li.indexOf('#');
		int notepos2 = li.indexOf("//");
		if (notepos1 < 0) notepos1 = li.length();
		if (notepos2 < 0) notepos2 = li.length();
		int notepos = Math.min(notepos1, notepos2);
		if (notepos == 0) return "";
		return li.substring(0, notepos);
	}

	/** whether the string is number
	 * 
	 * @param str
	 * @return */
	public static boolean isNumber (final String str) {
		return str.matches("[0-9.]");
	}

	/** whether two strings are equal
	 * 
	 * @param comparedString1
	 * @param comparedString2
	 * @return */
	public static boolean phraseEquals (String comparedString1, String comparedString2) {
		//
		String slimString1 = "";
		String slimString2 = "";

		//
		String stringBuf1 = comparedString1.trim().toLowerCase();
		String stringBuf2 = comparedString2.trim().toLowerCase();

		//
		if (stringBuf1.contains("	") || stringBuf1.contains(" ")) {
			//
			String[] wordList1 = stringBuf1.split("\\s+");
			//
			for (String word : wordList1) {
				slimString1 = slimString1 + word.trim();
			}
		} else {
			slimString1 = stringBuf1;
		}

		//
		if (stringBuf2.contains("	") || stringBuf2.contains(" ")) {
			//
			String[] wordList2 = stringBuf2.split("\\s+");
			//
			for (String word : wordList2) {
				slimString2 = slimString2 + word.trim();
			}
		} else {
			slimString2 = stringBuf2;
		}

		//
		if (slimString1.contentEquals(slimString2)) {
			//
			return true;
		} else {
			//
			return false;
		}
	}

	/** whether the words is in English
	 *
	 * @param words
	 * @return */
	public static boolean isEnglish (String words) {
		boolean sign = true;

		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(words);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");

		for (int i = 0; i < temp.length(); i++) {
			if (!(temp.charAt(i) >= 'A' && temp.charAt(i) <= 'Z') && !(temp.charAt(i) >= 'a' && temp.charAt(i) <= 'z')) {
				sign = false;
			}
		}
		return sign;
	}

	/** extract message from brackets or remove it
	 *
	 * @param msg
	 * @return */
	public static String extractMessageOrRemove (String msg, char left, char right, boolean remove) {

		int start = 0;
		int startFlag = 0;
		int endFlag = 0;
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == left) {
				startFlag++;
				if (startFlag == endFlag + 1) {
					start = i;
				}
			} else if (msg.charAt(i) == right) {
				endFlag++;
				if (endFlag == startFlag) {
					StringBuffer stringBuffer = new StringBuffer();
					if (!remove) {
						msg = msg.substring(start + 1, i);
						break;
					} else {
						stringBuffer.append(msg.substring(0, start)).append(msg.substring(i + 1, msg.length()));

						msg = stringBuffer.toString();
						i = start;
					}
				}
			}
		}
		return msg;
	}
}
